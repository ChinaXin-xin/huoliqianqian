package xin.common.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.log4j.Log4j2;
import xin.common.enmu.SmsType;
import xin.h5.exception.SmsSendingFailedException;

/**
 * @ClassName:SendSmsUtil
 * @Description: 阿里云短信工具类
 * @author: TracyYang
 * @date:2019年8月28日 上午10:15:40
 */
@Log4j2
public class SendSmsUtil {

    public static RedisCache redisCache;

    // 签名
    private static final String signName = "活利钱线";

    // 阿里云短信配置信息
    private static final String accessKeyId = "LTAI5tBBjXVz5dZd5K3HcCGg";
    // 定义一个字符串常量来存储访问密钥，用于身份验证和授权。
    private static final String accessKeySecret = "MKpDOGUzDQyORzzwnJs7FzOhXzTgLR";

    // 指定阿里云服务的区域ID，这里是"cn-hangzhou"，表示杭州区域。
    private static final String REGION_ID = "cn-hangzhou";

    // 定义产品名称，这里是"Dysmsapi"，表示阿里云的短信服务。
    private static final String PRODUCT = "Dysmsapi";

    // 指定服务的域名，这里是"dysmsapi.aliyuncs.com"，是阿里云短信服务的API入口。
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";


    /**
     * 发送短信通知
     *
     * @param mobile  手机号
     * @param code    六位数验证码
     * @param smsType 验证码的类型
     * @return 执行结果
     */
    public static boolean sendSMS(String mobile, String code, SmsType smsType) throws SmsSendingFailedException {

        String templateCode;
        if (smsType == SmsType.LOGIN_CONFIRMATION) {
            templateCode = "SMS_215797608";  //登录
        } else if (smsType == SmsType.CHANGE_PASSWORD) {
            templateCode = "SMS_215822490";  //修改密码
        } else if (smsType == SmsType.USER_REGISTRATION) {
            templateCode = "SMS_215730790";  //注册
        } else {
            log.error("smsType类型错误！");
            return false;
        }

        try {
            IClientProfile profile = DefaultProfile.getProfile(REGION_ID, accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint(REGION_ID, REGION_ID, PRODUCT, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            request.setPhoneNumbers(mobile);
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            request.setTemplateParam("{\"code\":\"" + code + "\"}");
            System.out.println("{\"code\":\"" + code + "\"}");

            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if ((sendSmsResponse.getCode() != null) && (sendSmsResponse.getCode().equals("OK"))) {
                log.info("发送成功,code:" + sendSmsResponse.getCode());
                return true;
            } else {
                log.info("发送失败,code:" + sendSmsResponse.getCode());
                throw new SmsSendingFailedException("阿里云验证码发送失败, 响应码: " + sendSmsResponse.getCode());
            }
        } catch (ClientException e) {
            log.error("发送失败,系统错误！", e);
            throw new SmsSendingFailedException("发送失败,系统错误！", e);
        }
    }


    public static void main(String[] args) throws SmsSendingFailedException {

        System.out.println(SendSmsUtil.sendSMS("13721776906", "333333", SmsType.USER_REGISTRATION));
    }
}

