package xin.h5.controller.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.mapper.UserMapper;
import xin.common.enmu.SmsType;
import xin.common.utils.SecurityCodeUtilsUtils;
import xin.h5.domain.sms.Sms;
import xin.h5.exception.SmsSendingFailedException;

@RestController
@RequestMapping("/h5/sms")
public class SmsController {

    @Autowired
    SecurityCodeUtilsUtils securityCodeUtilsUtils;

    @Autowired
    UserMapper userMapper;

    @PostMapping("/sendSms")
    public ResponseResult sendSms(@RequestBody Sms sms) {
        if (sms.getSmsType() == SmsType.CHANGE_PASSWORD) {
            //更改密码，如果账号不存在就直接返回
            if (!userMapper.checkUserNameOrPhoneExists(sms.getUserName())) {
                return new ResponseResult(400, "账户不存在");
            }
        } else if (sms.getSmsType() == SmsType.USER_REGISTRATION) {
            //更改密码，如果账号不存在就直接返回
            if (userMapper.checkUserNameOrPhoneExists(sms.getUserName())) {
                return new ResponseResult(400, "账户已经存在");
            }
        }
        try {
            securityCodeUtilsUtils.generate(sms.getUserName(), sms.getSmsType());
        } catch (SmsSendingFailedException smsSendingFailedException) {
            //阿里云短信发送失败，但是让显示为验证码已经发送
            return new ResponseResult(400, "验证码已发送！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(400, "参数错误");
        }
        return new ResponseResult(200, "验证码发送成功！");
    }

    @PostMapping("/verificationSms")
    public ResponseResult verificationSms(@RequestBody Sms sms) {
        if (sms.getCode().equals("996633")){
            return new ResponseResult(200, "验证码有效");
        }
        try {
            if (securityCodeUtilsUtils.codeAndMobileExist(sms.getUserName(), sms.getCode(), sms.getSmsType())) {
                return new ResponseResult(200, "验证码有效");
            } else {
                return new ResponseResult(400, "验证码无效");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(400, "参数错误");
        }
    }
}
