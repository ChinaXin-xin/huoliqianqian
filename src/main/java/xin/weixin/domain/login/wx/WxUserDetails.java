package xin.weixin.domain.login.wx;

import lombok.Data;

/**
 * 微信返回的部分用户信息
 */

@Data
public class WxUserDetails {
    // 错误消息：如果请求成功，通常为 "getPhoneNumber:ok"
    private String errMsg;

    // 加密数据：包含敏感信息如用户的手机号码，需要使用session_key进行解密
    private String encryptedData;

    // 加密算法的初始向量：用于解密encryptedData
    private String iv;

    // 用户的登录凭证（code）：在用户登录时获取，用于请求session_key和openid
    private String code;

    // 用于解密的session_key
    private String session_key;

    // 用户的openid
    private String openid;
}
