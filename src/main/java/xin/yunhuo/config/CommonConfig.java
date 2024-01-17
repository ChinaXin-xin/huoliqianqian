package xin.yunhuo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author Huangdaye
 * @Desc config.properties配置读取
 * @Date 2020/7/8 15:27
 */
public class CommonConfig {

    /** 客户私钥 **/
    public static String PRIVATE_KEY="MIICdgIBAD7Lp7g5s5AX4fvd9qowKG4gUzgRWMnHoc7AAoGBAL6Tv9NGaLS87RO+xQdx4/fqOuV/u7dKCY0VhmfEdD/yaylvMWOxk+HL9iAFh3rrMuVtwPiGSvZfebGPI3XGFeH6qh/XD7y49WiEyJwdvKQrAj3XYBENs9t+9kJa+4WOgbBfQ9hpT8whXhzzvfskCo0fwWqyFU5gjulHMwIY+EKlAgMBAAECgYB5RD7dy4G48DbTR6LMF/Z9vHf5rQvfl6b4NWgvA10Ni84QsYZ1PsqWUnTkBMRd8q3xnBsQ3ynKX1g3Ch5RCYbCCZ9lcgy4MyS8qqSbS+niHBwTxKXOXgOeJvYUL9w20AAZSsYWTuNJT1xkL9jslBScMHpuz6vbboE4SHBnZPUyyQJBAOSzoPWATD7Lp7g5s5AX4fvd9qowKG4gUzgRWMnHoc7HrPWHNZatpUdnGRkEzGB2bB58OHsACu5T9wIiFblpee8CQQD7Lp7g5s5AX4fvd9qowKG4gUzgRWMnHoc78C320/5Ma+LcGz2QQ2Rh+lUxI3RyBgnqmh/leEaDbVpyfS0EyTD7Lp7g5s5AX4fvd9qowKG4gUzgRWMnHoc79SdRo4hg9lpUxRA0uSX1UgmVoN3BEU6hEzgVrtQzmsedsEPE58wQlfHHDQJAGVckk+CukEzfcSpEyj4FMWm+q9PouXcteZGSVDTAi4FxVGYj/QQUY5MiFZE5nuXavbSLP233KluOF/WIqH3huwJAJnKaaGbL0GbkOKTD7Lp7g5s5AX4fvd9qowKG4gUzgRWMnHoc7gypOcxcbbhTvj+F6JXwyMJStpfQv40Mv9IwGg==";

    /**  云获公钥 **/
    public static String PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMOjbCr5G/YRCVeyhQWkUY6QhgbbQPjN2Ut3vA3yHGRRb+VuH/D7Lp7g5s5AX4fvd9qowKG4gUzgRWMnHoc7kVh1zIfNekfsXW50XUqdtCC6EWd7e7bpA5jWQmsiQKCAJa9O7JsdCJJ9kqS/vOfk/NPqILKd4wxtFEsF0EOSXMswIDAQAB";

    /**  云获配置生成的clientId **/
    public static String CLIENT_ID="76cml5qv4vcs2f0eucw3au48";

    /**  云获配置生成的client_secret **/
    public static String CLIENT_SECRET="dm9xcv7wvfh3cxtzwvq038p17vsd94i7";

    /**  token请求的url **/
    public static String TOKEN_URL="http://web.yunhuotec.com/api/oauth/anno/token";

    static {
        InputStream in = CommonConfig.class.getClassLoader().getResourceAsStream("yunhuoConfig.properties");
        Properties p = new Properties();
        try {
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PRIVATE_KEY = p.getProperty("privateKey");
        PUBLIC_KEY = p.getProperty("publicKey");
        CLIENT_ID=p.getProperty("client_id");
        CLIENT_SECRET=p.getProperty("client_secret");
        TOKEN_URL=p.getProperty("token_url");
    }

    public static void main(String[] args) {
        System.out.println(PRIVATE_KEY);
        System.out.println(PUBLIC_KEY);
        System.out.println(CLIENT_ID);
        System.out.println(CLIENT_SECRET);
        System.out.println(TOKEN_URL);
    }

}
