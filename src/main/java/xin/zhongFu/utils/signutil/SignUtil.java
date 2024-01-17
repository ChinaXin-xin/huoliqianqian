package xin.zhongFu.utils.signutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import xin.zhongFu.constants.EncodingConstants;
import xin.zhongFu.utils.StringUtils;

import java.util.*;

@Slf4j
public class SignUtil {

    /**
     * 签名
     *
     * @param channelKey
     * @param map
     * @return
     */
    public static String signByMap(String channelKey, TreeMap<String, Object> map) {
        return signByMap(channelKey, map, EncodingConstants.UTF_8_ENCODING);
    }

    /**
     * 签名
     *
     * @param map
     * @return
     */
    public static String signByMap(String channelKey, TreeMap<String, Object> map, String charsetName) {
        try {
            StringBuilder sb = new StringBuilder();
            Iterator<String> iterator = map.keySet().iterator();
            sb.append(channelKey);
            while (iterator.hasNext()) {
                Object key = iterator.next();
                Object valueObj = map.get(key);
                if (valueObj != null) {
                    //并将获取的值进行拼接
                    String value = valueObj.toString();
                    sb.append(value);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("SignData orig Data : " + sb.toString());
            }
            String signData = EncryptUtil.md5Encrypt(sb.toString(), charsetName);
            if (log.isDebugEnabled()) {
                log.debug("SignData : " + signData);
            }
            return signData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证签名
     *
     * @param map
     * @param sign
     * @return
     */
    public static boolean signVerify(String channelKey, TreeMap<String, Object> map, String sign) {
        boolean result = false;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(channelKey);
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                // 迭代key 拼接value
                Object valueObj = map.get(iterator.next());
                if (valueObj != null) {
                    sb.append(valueObj.toString());
                }
            }
            String md5Result = EncryptUtil.md5Encrypt(sb.toString());
            log.info("signData:{},sign:{}", map.toString(), md5Result);
            if (sign.equalsIgnoreCase(md5Result)) {
                result = true;
            }
        } catch (Exception e) {
            log.error("验证签名失败", e);
        }
        return result;
    }

    /**
     * 验证签名
     *
     * @param requestWrapper 网关请求包装类
     * @param signKey        签名密钥
     * @return 是否验签成功
     */
    public static boolean signVerify(RequestWrapper requestWrapper, String signKey) {
        List<String> ignoreSignList = new ArrayList<>();
        ignoreSignList.add("sign");
        ignoreSignList.add("imageList");

        return signVerify(requestWrapper, signKey, ignoreSignList);
    }

    /**
     * 验证签名+忽略签名属性
     *
     * @param requestWrapper 网关请求包装类
     * @param signKey        签名密钥
     * @param ignoreList     忽略签名属性
     * @return 是否验签成功
     */
    public static boolean signVerify(RequestWrapper requestWrapper, String signKey, List<String> ignoreList) {
        String body = requestWrapper.getBody();
        // 没有请求参数或者“{}”时，不验证签名
        if(StringUtils.isBlank(body) || "{}".equals(body)) {
        	return true;
        }
        JSONObject json = JSON.parseObject(body);
        //验证报文mac
        TreeMap<String, Object> signMap = new TreeMap<>();
        for (Object map : json.entrySet()) {
            String key = ((Map.Entry) map).getKey().toString();
            Object value = ((Map.Entry) map).getValue();
            if (false == isIgnore(key, ignoreList)) {
                signMap.put(key, value);
            }
        }
        Object sign = json.get("sign");
        if (null == sign || StringUtils.isBlank(sign.toString())) {
            return false;
        }

        return signVerify(signKey, signMap, sign.toString());
    }

    /**
     * 判断是否或略参数
     *
     * @param key        参数名称
     * @param ignoreList 忽略里欸包
     * @return 成功忽略，失败不忽略
     */
    public static boolean isIgnore(String key, List<String> ignoreList) {
        if ((null != ignoreList) && !ignoreList.isEmpty()) {
            for (String ignore : ignoreList) {
                if (key.equalsIgnoreCase(ignore)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 验证签名-充值回调
     *
     * @param map
     * @param sign
     * @return
     */
    public static boolean simpleSignVerify(String channelKey, TreeMap<String, Object> map, String sign) {
        if (log.isDebugEnabled()) {
            log.debug("verify sign start ! sign :" + sign);
        }
        try {
            StringBuilder sb = new StringBuilder();
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                Object key = iterator.next();
                //并将获取的值进行拼接
                Object valueObj = map.get(key);
                if (valueObj != null) {
                    String value = valueObj.toString();
                    if (log.isDebugEnabled()) {
                        log.debug("map:" + key + ":" + value);
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                    sb.append("&");
                }
            }
            sb.append("key=");
            sb.append(channelKey);
            String md5Result = EncryptUtil.md5Encrypt(sb.toString());
            md5Result = md5Result.toUpperCase();
            if (sign.equalsIgnoreCase(md5Result)) {
                if (log.isDebugEnabled()) {
                    log.debug("verify success");
                }
                return true;
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("verify failure");
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 签名
     *
     * @param map
     * @return
     */
    public static String simpleSignByMap(String channelKey, TreeMap<String, Object> map, String charsetName) {
        try {
            StringBuilder sb = new StringBuilder();
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                Object key = iterator.next();
                Object valueObj = map.get(key);
                if (valueObj != null) {
                    //并将获取的值进行拼接
                    String value = valueObj.toString();
                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                    sb.append("&");
                }
            }           
            sb.append("key=");
            sb.append(channelKey);
            if (log.isDebugEnabled()) {
                log.debug("SignData orig Data : " + sb.toString());
            }
            String signData = EncryptUtil.md5Encrypt(sb.toString(), charsetName);
            signData = signData.toUpperCase();
            if (log.isDebugEnabled()) {
                log.debug("SignData : " + signData);
            }
            return signData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
