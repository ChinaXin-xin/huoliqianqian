package xin.weixin.domain.login.wx;

import lombok.Data;


/**
 * 微信的登录凭证
 */
@Data
public class LoginCertificate {
    private String openid;
    private String session_key;
}
