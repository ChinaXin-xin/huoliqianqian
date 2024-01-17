package xin.weixin.domain.login.wx;

import lombok.Data;


/**
 * 从微信解密出来的手机号
 */

/**
 * 电话号码详情实体类
 */
@Data
public class WxPhoneNumberDetails {
    /**
     * 完整电话号码
     */
    private String phoneNumber;

    /**
     * 无国家代码的电话号码
     */
    private String purePhoneNumber;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 应用程序ID
     */
    private String appid;
}
