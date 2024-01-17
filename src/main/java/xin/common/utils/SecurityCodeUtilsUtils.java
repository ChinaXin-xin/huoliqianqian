package xin.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xin.common.enmu.SmsType;
import xin.h5.exception.SmsSendingFailedException;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 根据类型生成验证码（登录，注册，修改棉麻），而且防止重复
 * 存储的时候，键：字符前缀+验证码，值：手机号
 * 这样能先判断这个验证码是否已经存在，被用于其它用户验证
 */
@Component
public class SecurityCodeUtilsUtils {

    @Autowired
    RedisCache redisCache;

    //验证码有效时间，单位秒，五分钟
    Integer validTime = 300;

    /**
     * 生成六位数验证码
     *
     * @return
     */
    public static String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));  // 生成一个0到9的随机数，并添加到字符串构建器中
        }

        return code.toString();
    }

    /**
     * 提取字符串中的最后六位数字作为验证码。
     *
     * @param input 输入字符串。
     * @return 验证码，如果输入不符合要求则返回空字符串。
     */
    public static String extractVerificationCode(String input) {
        if (input == null || input.length() < 6) {
            return "";
        }

        String lastSixChars = input.substring(input.length() - 6);
        if (lastSixChars.matches("\\d{6}")) {
            return lastSixChars;
        }

        return "";
    }

    /**
     * 判断验证码是否存在
     *
     * @param code    验证码
     * @param smsType 验证码类型
     * @return
     */
    public Boolean codeIsExist(String code, SmsType smsType) {
        //从redis中读取验证码
        String mobile = "";
        if (smsType == SmsType.LOGIN_CONFIRMATION) {      //登录
            Long value = redisCache.getCacheObject(PrefixLabelStringUtils.LOGIN_CONFIRMATION_prefix + code);
            mobile = (value != null) ? Long.toString(value) : null;
        } else if (smsType == SmsType.CHANGE_PASSWORD) {  //修改密码
            Long value = redisCache.getCacheObject(PrefixLabelStringUtils.CHANGE_PASSWORD_prefix + code);
            mobile = (value != null) ? Long.toString(value) : null;
        } else if (smsType == SmsType.USER_REGISTRATION) { //注册
            Long value = redisCache.getCacheObject(PrefixLabelStringUtils.USER_REGISTRATION + code);
            mobile = (value != null) ? Long.toString(value) : null;
        } else {
            System.out.println("smsType类型错误！");
            return false;
        }

        if (mobile == null || mobile.isEmpty())
            return false;
        return true;
    }

    /**
     * 吧传入的手机号与验证码匹配，看看是否一样
     *
     * @param code
     * @param smsType
     * @return
     */
    public Boolean codeAndMobileExist(String userMobile, String code, SmsType smsType) {
        //从redis中读取验证码
        String mobile = "";
        if (smsType == SmsType.LOGIN_CONFIRMATION) {      // 登录
            Object value = redisCache.getCacheObject(PrefixLabelStringUtils.LOGIN_CONFIRMATION_prefix + code);
            mobile = (value != null) ? String.valueOf(value) : null;
        } else if (smsType == SmsType.CHANGE_PASSWORD) {  // 修改密码
            Object value = redisCache.getCacheObject(PrefixLabelStringUtils.CHANGE_PASSWORD_prefix + code);
            mobile = (value != null) ? String.valueOf(value) : null;
        } else if (smsType == SmsType.USER_REGISTRATION) { // 注册
            Object value = redisCache.getCacheObject(PrefixLabelStringUtils.USER_REGISTRATION + code);
            mobile = (value != null) ? String.valueOf(value) : null;
        } else {
            System.out.println("smsType类型错误！");
            return false;
        }
        if (mobile == null || mobile.isEmpty())
            return false;
        if (mobile.equals(userMobile))
            return true;
        return false;
    }

    /**
     * 用于生成短信验证码
     * 这个根据类型生成的的验证码，不会有重复的
     *
     * @return
     */
    public String generateCode(SmsType smsType) {
        //存储产生用户的验证码
        String userCode = "";
        //判断验证码是否存在
        Boolean aBoolean = true;
        if (smsType == SmsType.LOGIN_CONFIRMATION) {      //登录
            //如果验证码存在就继续运行
            while (aBoolean) {
                //产生用户的验证码
                userCode = generateCode();
                aBoolean = codeIsExist(userCode, smsType);
            }
        } else if (smsType == SmsType.CHANGE_PASSWORD) {  //修改密码
            //如果验证码存在就继续运行
            while (aBoolean) {
                //产生用户的验证码
                userCode = generateCode();
                aBoolean = codeIsExist(userCode, smsType);
            }
        } else if (smsType == SmsType.USER_REGISTRATION) { //注册
            //如果验证码存在就继续运行
            while (aBoolean) {
                //产生用户的验证码
                userCode = generateCode();
                aBoolean = codeIsExist(userCode, smsType);
            }
        } else {
            System.out.println("smsType类型错误！");
        }
        System.out.println("生成验证码：" + userCode);
        return userCode;
    }

    /**
     * 用户发送验证码
     *
     * @param smsType
     * @return
     */
    public Boolean generate(String mobile, SmsType smsType) throws SmsSendingFailedException {
        String userCode = "";
        if (smsType == SmsType.LOGIN_CONFIRMATION) {      //登录
            userCode = generateCode(smsType);
            SendSmsUtil.sendSMS(mobile, userCode, smsType);
            System.out.println("----------------");
            System.out.println(PrefixLabelStringUtils.LOGIN_CONFIRMATION_prefix + userCode);
            redisCache.setCacheObject(PrefixLabelStringUtils.LOGIN_CONFIRMATION_prefix + userCode, mobile, validTime, TimeUnit.SECONDS);
        } else if (smsType == SmsType.CHANGE_PASSWORD) {  //修改密码
            userCode = generateCode(smsType);
            SendSmsUtil.sendSMS(mobile, userCode, smsType);
            System.out.println("----------------");
            System.out.println(PrefixLabelStringUtils.CHANGE_PASSWORD_prefix + userCode);
            redisCache.setCacheObject(PrefixLabelStringUtils.CHANGE_PASSWORD_prefix + userCode, mobile, validTime, TimeUnit.SECONDS);
        } else if (smsType == SmsType.USER_REGISTRATION) { //注册
            userCode = generateCode(smsType);
            SendSmsUtil.sendSMS(mobile, userCode, smsType);
            System.out.println("----------------");
            System.out.println(PrefixLabelStringUtils.USER_REGISTRATION + userCode);
            redisCache.setCacheObject(PrefixLabelStringUtils.USER_REGISTRATION + userCode, mobile, validTime, TimeUnit.SECONDS);
        } else {
            System.out.println("generate函数 smsType类型错误！");
            return false;
        }
        return true;
    }

    public Boolean verification(String mobile, String code, SmsType smsType) {
        //如果数据库中没有这个验证码
        if (!codeIsExist(code, smsType)) {
            return false;
        }
        return codeAndMobileExist(mobile, code, smsType);
    }

    public static void main(String[] args) {

    }
}
