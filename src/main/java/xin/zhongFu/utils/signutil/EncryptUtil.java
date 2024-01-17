package xin.zhongFu.utils.signutil;


import lombok.extern.slf4j.Slf4j;
import xin.zhongFu.constants.EncodingConstants;
import xin.zhongFu.constants.KeyConstants;
import xin.zhongFu.exception.AcqBizException;
import xin.zhongFu.utils.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * DES加密工具类
 */
@Slf4j
public class EncryptUtil {
    /**
     * 填充模式(PKCS5Padding)   选择不自动应用任何填充(NoPadding)
     */
    private static final String MODEL = "DESede/ECB/PKCS5Padding";
    private static final String MODEL1 = "DESede/ECB/NOPadding";

    /**
     * DES解密
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String desDecrypt(String message, String key){
        try {
            byte[] keyBytes = null;
            if (key.length() == KeyConstants.KEY_LENGTH_16) {
                keyBytes = newInstance8Key(ByteUtil.convertHexString(key));
            } else if (key.length() == KeyConstants.KEY_LENGTH_32) {
                keyBytes = newInstance16Key(ByteUtil.convertHexString(key));
            } else if (key.length() == KeyConstants.KEY_LENGTH_48) {
                keyBytes = newInstance24Key(ByteUtil.convertHexString(key));
            }
            SecretKey deskey = new SecretKeySpec(keyBytes, "DESede");
            Cipher c1 = Cipher.getInstance(MODEL);
            c1.init(2, deskey);
            byte[] retByte = c1.doFinal(ByteUtil.convertHexString(message));
            return new String(retByte);
        } catch (Exception e) {
            log.error("解密失败:{}", e);
        }
        return null;
    }

    /**
     * DES加密
     *
     * @param message 要要加密的数据
     * @param key 加密使用的key
     * @return 加密后的数据
     * @throws AcqBizException
     */
    public static String desEncrypt(String message, String key) throws AcqBizException {
        byte[] keyBytes = null;
        if (key.length() == 16) {
            keyBytes = newInstance8Key(ByteUtil.convertHexString(key));
            System.out.println("EncryptUtil--desEncrypt:keyBytes16:"+new String(keyBytes));
        } else if (key.length() == 32) {
            keyBytes = newInstance16Key(ByteUtil.convertHexString(key));
            System.out.println("EncryptUtil--desEncrypt:keyBytes32:"+keyBytes);
        } else if (key.length() == 48) {
            keyBytes = newInstance24Key(ByteUtil.convertHexString(key));
            System.out.println("EncryptUtil--desEncrypt:keyBytes48:"+keyBytes);
        }
        SecretKey deskey = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = null;
        byte[] bytes = null;
        try {
            cipher = Cipher.getInstance(MODEL);
            cipher.init(1, deskey);
            bytes = cipher.doFinal(message.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new AcqBizException("无效的算法:message:" + message, e);
        } catch (NoSuchPaddingException e) {
            throw new AcqBizException("不支持的格式:message:" + message, e);
        } catch (InvalidKeyException e) {
            throw new AcqBizException("无效的key:" + key, e);
        } catch (IllegalBlockSizeException e) {
            throw new AcqBizException("数据长度错误:message:" + message, e);
        } catch (BadPaddingException e) {
            throw new AcqBizException("无效的格式:message:" + message, e);
        }
        catch (UnsupportedEncodingException e) {
            throw new AcqBizException("不支持的编码:message:" + message, e);
        }

        return ByteUtil.toHexString(bytes);
    }

    /**
     * MD5加码 生成32位md5码
     *
     * @param message
     * @return
     */
    public static String md5Encrypt(String message) {
        return md5Encrypt(message, EncodingConstants.UTF_8_ENCODING);
    }

    /***
     * MD5加码 生成32位md5码
     */
    public static String md5Encrypt(String message, String charsetName) {
        if (StringUtils.isBlank(charsetName)) {
            charsetName = EncodingConstants.UTF_8_ENCODING;
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        byte[] md5Bytes = new byte[0];
        try {
            md5Bytes = md5.digest(message.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            log.error("签名数据编码格式转换出错", e);
            return "";
        }
        String hexValue = ByteUtil.toHexString(md5Bytes);
        return hexValue;

    }


    private static byte[] newInstance24Key(byte[] key) {
        if ((key != null) && (key.length == 24)) {
            return key;
        }
        System.err.println("密钥长度有误,期望值[24]");
        return null;
    }

    private static byte[] newInstance16Key(byte[] key) {
        if ((key != null) && (key.length == 16)) {
            byte[] b = new byte[24];
            System.arraycopy(key, 0, b, 0, 16);
            System.arraycopy(key, 0, b, 16, 8);
            key = (byte[]) null;
            return b;
        }
        System.err.println("密钥长度有误,期望值[16]");
        return null;
    }

    private static byte[] newInstance8Key(byte[] key) {
        if ((key != null) && (key.length == 8)) {
            byte[] b = new byte[24];
            System.arraycopy(key, 0, b, 0, 8);
            System.arraycopy(key, 0, b, 8, 8);
            System.arraycopy(key, 0, b, 16, 8);
            key = (byte[]) null;
            return b;
        }
        System.err.println("密钥长度有误,期望值[8]");
        return null;
    }

    //生成交易交易值
    private static String check(String checkValue,String key) {
        String ek=null;
        try {
            ek=EncryptUtil.desEncrypt(checkValue,key);
            //System.out.println(ek);
        } catch (AcqBizException e) {
            e.printStackTrace();
        }
        return ek;
    }


    /**
     * 随机生成密钥
     */
    public static String generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
            keyGenerator.init(new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            //String key = HexUtil.byte2hex(secretKey.getEncoded());
            String key = ByteUtil.bytesToHexString(secretKey.getEncoded());
            // LOG.info("# key={}", key);
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //LOG.error("# 随机生成密钥失败, error msg={}", e.getLocalizedMessage());
            return null;
        }
    }


    /**
     * @title: desDecryptEx
     * @description: 加密平台使用-解密
     * @author: nm
     * @date: Create at 2022/4/25 17:26
     * @param message
     * @param key
     * @return: java.lang.String
     */
    public static String desDecryptEx(String message, String key){
        try {
            byte[] keyBytes = null;
            if (key.length() == KeyConstants.KEY_LENGTH_16) {
                keyBytes = newInstance8Key(ByteUtil.convertHexString(key));
            } else if (key.length() == KeyConstants.KEY_LENGTH_32) {
                keyBytes = newInstance16Key(ByteUtil.convertHexString(key));
            } else if (key.length() == KeyConstants.KEY_LENGTH_48) {
                keyBytes = newInstance24Key(ByteUtil.convertHexString(key));
            }
            SecretKey deskey = new SecretKeySpec(keyBytes, "DESede");
            Cipher c1 = Cipher.getInstance(MODEL1);
            c1.init(2, deskey);
            byte[] retByte = c1.doFinal(ByteUtil.convertHexString(message));
            return ByteUtil.bytesToHexString(retByte);
        } catch (Exception e) {
            log.error("解密失败:{}", e);
        }
        return null;
    }

    /**
     * @title: desEncryptEx
     * @description: 加密平台使用-加密
     * @author: nm
     * @date: Create at 2022/4/25 17:28
     * @param message
     * @param key
     * @return: java.lang.String
     */
    public static String desEncryptEx(String message, String key) throws AcqBizException {
        byte[] keyBytes = null;
        if (key.length() == 16) {
            keyBytes = newInstance8Key(ByteUtil.convertHexString(key));
        } else if (key.length() == 32) {
            keyBytes = newInstance16Key(ByteUtil.convertHexString(key));
        } else if (key.length() == 48) {
            keyBytes = newInstance24Key(ByteUtil.convertHexString(key));
        }
        SecretKey deskey = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = null;
        byte[] bytes = null;
        try {
            cipher = Cipher.getInstance(MODEL1);
            cipher.init(1, deskey);
            bytes = cipher.doFinal(ByteUtil.convertHexString(message)/*message.getBytes("UTF-8")*/);
        } catch (NoSuchAlgorithmException e) {
            throw new AcqBizException("无效的算法:message:" + message, e);
        } catch (NoSuchPaddingException e) {
            throw new AcqBizException("不支持的格式:message:" + message, e);
        } catch (InvalidKeyException e) {
            throw new AcqBizException("无效的key:" + key, e);
        } catch (IllegalBlockSizeException e) {
            throw new AcqBizException("数据长度错误:message:" + message, e);
        } catch (BadPaddingException e) {
            throw new AcqBizException("无效的格式:message:" + message, e);
        }
        return ByteUtil.toHexString(bytes).toUpperCase();
    }


    //加密pin
    private static String getPan(String pin,String strXorRlt) {

        //按照卡号要求进行截取
        String strPanblock = strXorRlt.substring(strXorRlt.length()-1-12, strXorRlt.length()-1);
        byte[] panBlock = new byte[8];
        System.arraycopy(StringUtils.hexStr2Bytes(strPanblock), 0, panBlock, 2, 6);

        //String Spinblock = "06" + pin + "FFFFFFFFFFFFFFFFFFFFFFFF";
        int pinLen = pin.length();
        String len = String.format("%02d",pinLen);
        String tmp = len.concat(pin);
        StringBuilder sb = new StringBuilder();
        sb.append(tmp);
        //右补齐
        while(true) {
            sb.append("F");
            if(16 == sb.length() ) {
                break;
            }
        }
        String spinblock = sb.toString();
        System.out.println( "here ： " + spinblock);

        byte[] pinBlock = StringUtils.hexStr2Bytes(spinblock);

        // 异或
        byte[] xorRlt = new byte[8];
        for (int i = 0; i < xorRlt.length; i++) {
            xorRlt[i] = (byte) (panBlock[i] ^ pinBlock[i]);
        }
        String pan =  StringUtils.byte2HexStr(xorRlt);
        return pan;
    }

    //解密pin
    private static String getPin(String strPinBlock,String strXorRlt) {
        //按照卡号要求进行截取
        String strPanblock = strXorRlt.substring(strXorRlt.length()-1-12, strXorRlt.length()-1);
        byte[] panBlock = new byte[8];
        System.arraycopy(StringUtils.hexStr2Bytes(strPanblock), 0, panBlock, 2, 6);
        byte[] pinBlock = StringUtils.hexStr2Bytes(strPinBlock);

        byte[] xorRlt = new byte[8];
        for (int i = 0; i < xorRlt.length; i++) {
            xorRlt[i] = (byte) (panBlock[i] ^ pinBlock[i]);
        }

        String tmp =  StringUtils.byte2HexStr(xorRlt);
        int i = Integer.valueOf(tmp.substring(0,2));
        String pin = tmp.substring(2,2+i);
        return pin;
    }

}
