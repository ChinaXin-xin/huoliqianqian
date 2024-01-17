package xin.weixin.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.weixin.domain.login.wx.LoginCertificate;
import xin.weixin.domain.login.wx.WxUserDetails;
import xin.weixin.service.login.WeChatLoginService;

@RestController
@RequestMapping("/pro")
public class VxLoginController {

    @Autowired
    WeChatLoginService weChatLoginService;

    @PostMapping("/getUserPortionMsg")
    public ResponseResult getUserPortionMsg(@RequestBody WxUserDetails details) {
        ResponseResult openId = weChatLoginService.getUserPortionMsg(details);
        System.out.println("getUserPortionMsg:  " + openId);
        return openId;
    }

    @PostMapping("/getSessionKeyAndOpenId")
    public ResponseResult<LoginCertificate> getSessionKeyAndOpenId(@RequestBody String code) {
        ResponseResult<LoginCertificate> test = weChatLoginService.getSessionKeyAndOpenId(code);
        System.out.println("getSessionKeyAndOpenId： " + test.getData());
        return test;
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody WxUserDetails details) {
        return weChatLoginService.login(details);
    }

    @PostMapping("/test")
    public ResponseResult login() {
        return new ResponseResult(200, "通过！");
    }
}
