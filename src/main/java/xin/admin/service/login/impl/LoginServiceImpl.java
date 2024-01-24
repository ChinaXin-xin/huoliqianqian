package xin.admin.service.login.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xin.admin.controller.sse.NotificationSSEController;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.mapper.UserMapper;
import xin.admin.service.login.LoginService;
import xin.admin.service.sse.impl.NotificationSSEServiceImpl;
import xin.admin.utils.JwtUtil;
import xin.admin.utils.MyStringUtils;
import xin.common.domain.User;
import xin.common.utils.RedisCache;
import xin.h5.domain.wallet.SysAward;
import xin.h5.mapper.wallet.SysAwardMapper;
import xin.level.service.UserGradationService;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    UserGradationService userGradationService;

    @Autowired
    SysAwardMapper sysAwardMapper;

    @Autowired
    NotificationSSEController notificationSSEController;

    @Override
    public ResponseResult login(User user, Boolean isAdmin) {
        if (!userMapper.checkUserNameOrPhoneExists(user.getUserName())) {
            return new ResponseResult(400, "账号不存在");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误...");
        }

        User curUser = ((LoginUser) authenticate.getPrincipal()).getUser();
        Date jwtDate = new Date();
        String userFlag = curUser.getUserName() + "++--++" + curUser.getPassword() + "++--++" + jwtDate;
        String jwt = JwtUtil.createJWT(userFlag);
        curUser.setJwt(jwtDate.toString());

        userMapper.updateById(curUser);

        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("Authorization", jwt);
        return new ResponseResult(200, "登陆成功", map);
    }

    @Override
    @Transactional
    public ResponseResult register(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        //创建自己专属的邀请码
        String myselfInvitationCode = "";
        while (true) {
            myselfInvitationCode = MyStringUtils.generateInvitationCode();
            if (!(userMapper.isInvitationCodeExist(myselfInvitationCode) > 0)) {
                break;
            }
        }
        user.setMyInvitationCode(myselfInvitationCode);

        User parentUser = userMapper.selectByInvitationCode(user.getInvitationCode());

        if (parentUser != null) {
            userGradationService.addChild(parentUser.getUserName(), user.getUserName());
        } else {
            //User admin = userMapper.selectById(3);

            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("user_type", 3);
            User admin = userMapper.selectOne(qw);

            userGradationService.addChild(admin.getUserName(), user.getUserName());
        }
        user.setCreateTime(new Date());
        user.setMyPoints(0f);
        user.setTransactionVolume(0f);
        userMapper.insert(user);

        SysAward sysAward = new SysAward();
        sysAward.setUid(user.getId());
        sysAwardMapper.insert(sysAward);

        NotificationSSEController.data.setCurrentVisitorsCount(NotificationSSEController.data.getCurrentVisitorsCount() + 1);
        notificationSSEController.sendNotification();
        return new ResponseResult(200, "注册成功");
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("1234"));
        ;
    }

    @Override
    public ResponseResult changePassword(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.changePassword(user);
        return new ResponseResult(200, "密码修改成功");
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("login:" + userid);
        return new ResponseResult(200, "退出成功");
    }
}
