package xin.weixin.service.login;

import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.weixin.domain.login.wx.LoginCertificate;
import xin.weixin.domain.login.wx.WxUserDetails;

@Service
public interface WeChatLoginService {

    //获取微信用户信息
    ResponseResult getUserPortionMsg(WxUserDetails details);


    public ResponseResult<LoginCertificate> getSessionKeyAndOpenId(String code);


    //获取openid和session_key
    public ResponseResult login(WxUserDetails details);
}
