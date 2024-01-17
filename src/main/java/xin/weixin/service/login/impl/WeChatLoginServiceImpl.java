package xin.weixin.service.login.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.mapper.UserMapper;
import xin.admin.utils.JwtUtil;
import xin.admin.utils.MyStringUtils;
import xin.common.domain.User;
import xin.weixin.domain.login.wx.LoginCertificate;
import xin.weixin.domain.login.wx.WxPhoneNumberDetails;
import xin.weixin.domain.login.wx.WxUserDetails;
import xin.weixin.feign.WeChatClient;
import xin.weixin.service.login.WeChatLoginService;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Service
public class WeChatLoginServiceImpl implements WeChatLoginService {

    @Autowired
    private WeChatClient weChatClient;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    private static final String APP_ID = "wx2b213ad5e5c4c2a8";
    private static final String APP_SECRET = "d736adeae0116b8b18975fb0da8ba48e";

    //微信用户同一密码，1234，因为要使用rrt的数据库
    private static final String wxUserPasswordEncode = "$2a$10$Kp1jWcSs1tq18zu4L2TfZuYbrcdRZB/itpwXRybnfgNsIkJBLqCfq";

    //微信用户密码，明文登录时使用
    private static final String wxUserPassword = "123456";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    //获取微信用户信息
    public ResponseResult getUserPortionMsg(WxUserDetails details) {
        String uxUserMsg = decryptData(details.getEncryptedData(), details.getSession_key(), details.getIv(), "UTF-8");
        return new ResponseResult(200, "查询成功！", uxUserMsg);
    }

    public static String decryptData(String data, String key, String iv, String encodingFormat) {
        try {
            byte[] dataByte = Base64.decodeBase64(data);
            byte[] keyByte = Base64.decodeBase64(key);
            byte[] ivByte = Base64.decodeBase64(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // 修改这里
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);

            if (resultByte != null && resultByte.length > 0) {
                return new String(resultByte, encodingFormat);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解密失败：" + e.getMessage(), e);
        }
    }


    public ResponseResult<LoginCertificate> getSessionKeyAndOpenId(String code) {
        String jsonResponse = weChatClient.getOpenId(APP_ID, APP_SECRET, code, "authorization_code");
        LoginCertificate loginCertificate;

        try {
            // 将JSON字符串解析为LoginCertificate对象
            loginCertificate = JSON.parseObject(jsonResponse, LoginCertificate.class);
        } catch (Exception e) {
            return null;
        }

        return new ResponseResult<>(200, "查询成功！", loginCertificate);
    }


    //获取openid和session_key
    public ResponseResult login(WxUserDetails details) {

        String wxPhoneMsg = decryptData(details.getEncryptedData(), details.getSession_key(), details.getIv(), "UTF-8");
        WxPhoneNumberDetails wxPhoneNumberDetails;

        String wxUserName;
        try {
            // 获取微信用户的手机号
            wxUserName = JSON.parseObject(wxPhoneMsg, WxPhoneNumberDetails.class).getPhoneNumber();
        } catch (Exception e) {
            return new ResponseResult(403, "登录错误！！");
        }

        User user = userMapper.selectByUserNameToUser(wxUserName);

        //如果是已经注册的用户，就直接登录
        if (user != null) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(wxUserName, wxUserPassword);
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            if (Objects.isNull(authenticate)) {
                throw new RuntimeException("用户名或密码错误...");
            }
            //使用userid生成token
            User curUser = ((LoginUser) authenticate.getPrincipal()).getUser();

            Date jwtDate = new Date();
            String userFlag = curUser.getUserName() + "++--++" + curUser.getPassword() + "++--++" + jwtDate;
            String jwt = JwtUtil.createJWT(userFlag);
            curUser.setJwt(jwtDate.toString());
            userMapper.updateById(curUser);

            //把token响应给前端
            HashMap<String, String> map = new HashMap<>();
            map.put("Authorization", jwt);
            return new ResponseResult(200, "登陆成功！", map);
        } else {
            User curUser = new User();
            curUser.setUserName(wxUserName);
            curUser.setPassword(wxUserPasswordEncode);
            curUser.setCreateTime(new Date());

            //创建自己专属的邀请码
            String myselfInvitationCode = "";
            while (true) {
                myselfInvitationCode = MyStringUtils.generateInvitationCode();
                if (!(userMapper.isInvitationCodeExist(myselfInvitationCode) > 0)) {
                    break;
                }
            }
            curUser.setMyInvitationCode(myselfInvitationCode);

            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("user_type", 0);
            User admin = userMapper.selectOne(qw);

            curUser.setInvitationCode(admin.getMyInvitationCode());

            Date jwtDate = new Date();
            String userFlag = curUser.getUserName() + "++--++" + curUser.getPassword() + "++--++" + jwtDate;
            String jwt = JwtUtil.createJWT(userFlag);
            curUser.setJwt(jwtDate.toString());

            userMapper.insert(curUser);

            //把token响应给前端
            HashMap<String, String> map = new HashMap<>();
            map.put("Authorization", jwt);
            return new ResponseResult(200, "登陆成功！", map);
        }
    }
}
