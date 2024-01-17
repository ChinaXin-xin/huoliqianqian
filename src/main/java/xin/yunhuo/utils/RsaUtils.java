package xin.yunhuo.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.alibaba.fastjson.JSONObject;
import xin.yunhuo.config.CommonConfig;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @Author Huangdaye
 * @Desc RSA工具类
 * @Date 2020/7/8 15:34
 */
public class RsaUtils {

    /**
     * @Author huangdaye
     * @Description 客户端加密(公钥加密)
     * @Date 15:34 2020/7/8
     * @Param [data]
     * @return java.lang.String
     **/
    public static String clientEncrypt(String data){
        RSA rsa = new RSA(null, CommonConfig.PUBLIC_KEY);
        byte[] encrypt =null;
        try {
            encrypt = rsa.encrypt(data.getBytes("UTF-8"), KeyType.PublicKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(encrypt);
    }

    /**
     * @Author huangdaye
     * @Description 客户端签名(私钥签名)
     * @Date 15:35 2020/7/8
     * @Param [data]
     * @return java.lang.String
     **/
    public static String clientSign(String data){
        Sign sign = new Sign(SignAlgorithm.MD5withRSA, CommonConfig.PRIVATE_KEY, null);
        byte[] encrypt = null;
        try {
            encrypt = sign.sign(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(encrypt);
    }

    /**
     * @Author huangdaye
     * @Description 解密
     * @Date 15:42 2020/7/8
     * @Param [encryptStr, privateKey]
     * @return java.lang.String
     **/
    public static String serverDecrypt(String encryptStr,String privateKey){
        RSA rsa = new RSA(privateKey, null);
        byte[] decrypt = new byte[0];
        try {
            decrypt = rsa.decrypt(Base64.getDecoder().decode(encryptStr), KeyType.PrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decrypt);
    }

    /**
     * @Author huangdaye
     * @Description 验签
     * @Date 15:42 2020/7/8
     * @Param [data, signData, publicKey]
     * @return boolean
     **/
    public static boolean serverVerifySign(String data,String signData,String publicKey){
        Sign sign = new Sign(SignAlgorithm.MD5withRSA, null, publicKey);
        boolean encrypt = false;
        try {
            encrypt = sign.verify(data.getBytes("UTF-8"), Base64.getDecoder().decode(signData));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encrypt;
    }

    /**
     * @Author huangdaye
     * @Description 解密处理返回的json数据
     * @Date 15:52 2020/7/9
     * @Param [json]
     * @return java.lang.String
     **/
    public static String decryptResJSONData(String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        //解密，验签
        String serverDecrypt = RsaUtils.serverDecrypt(jsonObject.getString("encrypt"), CommonConfig.PRIVATE_KEY);
        boolean verifySign = RsaUtils.serverVerifySign(serverDecrypt, jsonObject.getString("sign"), CommonConfig.PUBLIC_KEY);
        if(!verifySign){
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx返回密文验签失败");
            return null;
        }else {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<验签成功");
        }
        return serverDecrypt;
    }

    /**
     * @Author huangdaye
     * @Description 生成公私钥对的方法
     * @Date 9:54 2020/7/9
     * @Param []
     * @return void
     **/
    public static void createSecret(){
        KeyPair keyPair = SecureUtil.generateKeyPair(SignAlgorithm.MD5withRSA.getValue());
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        System.out.println("privateKey:"+Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println("publicKey:"+Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    }

    public static void main(String[] args) {
       createSecret();
    }

}
