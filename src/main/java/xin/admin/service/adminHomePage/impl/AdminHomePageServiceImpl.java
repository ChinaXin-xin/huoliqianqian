package xin.admin.service.adminHomePage.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.adminHomePage.UserMsgRequestQuery;
import xin.admin.domain.adminHomePage.UserPortionMsg;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.mapper.membershipManagement.UserAuthenticationInfoMapper;
import xin.admin.service.adminHomePage.AdminHomePageService;
import xin.admin.service.userInfomation.UserService;
import xin.common.domain.User;
import xin.level.service.UserGradationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminHomePageServiceImpl implements AdminHomePageService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    private UserAuthenticationInfoMapper userAuthenticationInfoMapper;

    @Autowired
    private UserGradationService userGradationService;

    @Override
    public ResponseResult<UserMsgRequestQuery> query(UserMsgRequestQuery requestQuery) {
        int temp = requestQuery.getPageNumber(); //保存一下，要不然返回的时候会乱，结束后再重新赋值
        // 计算从哪一行开始查询
        int startRow = (requestQuery.getPageNumber() - 1) * requestQuery.getQuantity();
        requestQuery.setPageNumber(startRow);

        List<User> users = userMapper.queryCommonUserMsgList(requestQuery.getUser(), requestQuery.getPageNumber(), requestQuery.getQuantity());

        for (User u : users) {
            u.setLevel(userService.getUserRealLevel(u.getId()));

            //自己有多少没有激活的机器
            u.setInactiveMachineryCount(sysPosTerminalMapper.selectByUidNoActivateNum(u.getId()));
        }

        requestQuery.setUserList(users);
        requestQuery.setCount(userMapper.queryCommonUserMsgCount(requestQuery.getUser()));
        requestQuery.setPageNumber(temp);
        return new ResponseResult<UserMsgRequestQuery>(200, "查询成功", requestQuery);
    }

    @Override
    public ResponseResult alterUserMsg(User user) {
        userMapper.updateById(user);
        return new ResponseResult(200, "修改成功！");
    }

    @Override
    public ResponseResult<User> selectUserMsgById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null)
            return new ResponseResult(200, "查询失败！");

        return new ResponseResult(200, "查询成功！", user);
    }


    /**
     * 用户管理->修改弹窗中的信息
     */
    @Override
    public ResponseResult<UserPortionMsg> queryUserPortionMsg(Long id) {

        UserPortionMsg userPortionMsg = userMapper.selectByIdToUserPortionMsg(id);

        //设置等级
        userPortionMsg.setLevel(userService.getUserRealLevel(id));

        //设置查询每个机型对应的数量
        userPortionMsg.setPosList(sysPosTerminalMapper.queryMyselfMachineTypeAndNumber(id));

        userPortionMsg.setId(id);

        //是否已经认证
        Boolean isAlready = userAuthenticationInfoMapper.isAuthentication(id);
        if (isAlready) {
            userPortionMsg.setIsAuthentication(true);
            UserAuthenticationInfo userAuthenticationInfo = userAuthenticationInfoMapper.selectByUid(id);
            userPortionMsg.setMemberPhone(userAuthenticationInfo.getMemberPhone());
            userPortionMsg.setMemberName(userAuthenticationInfo.getMemberName());
            userPortionMsg.setBankCard(userAuthenticationInfo.getBankCard());
            userPortionMsg.setBankBranch(userAuthenticationInfo.getBankBranch());
        }

        StringBuilder relation = new StringBuilder();
        User user = userMapper.selectById(id);

        String directParent = userGradationService.getDirectParent(user.getUserName());
        List<String> list = new ArrayList<>();
        while (directParent != null) {
            list.add(directParent);
            directParent = userGradationService.getDirectParent(directParent);
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            relation.append(list.get(i));
            if (i != 0) {
                relation.append("->");
            }
        }
        userPortionMsg.setRelation(relation.toString());
        return new ResponseResult<>(200, "查询成功！", userPortionMsg);
    }

    @Override
    public ResponseResult alterUserPortionMsg(UserPortionMsg userPortionMsg) {
        User user = new User();
        user.setNickname(userPortionMsg.getNickname());
        user.setId(userPortionMsg.getId());
        if (userPortionMsg.getNewPassword() != null && !userPortionMsg.getNewPassword().equals("")) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }

        if (userAuthenticationInfoMapper.isAuthentication(userPortionMsg.getId())) {
            UserAuthenticationInfo userAuthenticationInfo = new UserAuthenticationInfo();
            userAuthenticationInfo.setMemberName(userPortionMsg.getMemberName());
            userAuthenticationInfo.setMemberPhone(userPortionMsg.getMemberPhone());
            userAuthenticationInfo.setBankCard(userPortionMsg.getBankCard());
            userAuthenticationInfo.setBankBranch(userPortionMsg.getBankBranch());

            UpdateWrapper<UserAuthenticationInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("uid", userPortionMsg.getId());
            userAuthenticationInfoMapper.update(userAuthenticationInfo, updateWrapper);
        }
        userMapper.updateById(user);
        return new ResponseResult(200, "修改成功！");
    }
}
