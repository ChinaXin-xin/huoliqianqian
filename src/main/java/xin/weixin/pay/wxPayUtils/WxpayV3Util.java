package xin.weixin.pay.wxPayUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.v3.util.PemUtils;
import com.github.binarywang.wxpay.v3.util.SignUtils;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class WxpayV3Util {
    private static final String PAY_BASE_URL = "https://api.mch.weixin.qq.com";

    public static JSONObject unifiedOrderV3(String reqUrl, JSONObject reqJSON, WxPayService wxPayService) throws WxPayException {
        String response = wxPayService.postV3(PAY_BASE_URL + reqUrl, reqJSON.toJSONString());
        return JSONObject.parseObject(getPayInfo_(response, wxPayService.getConfig()));
    }

    public static String getPayInfo_(String response, WxPayConfig wxPayConfig)  throws WxPayException {
        try {
            JSONObject resJSON = JSON.parseObject(response);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
            String nonceStr = SignUtils.genRandomStr();
            String prepayId = resJSON.getString("prepay_id");

            Map<String, String> payInfo = new HashMap<>(8);

            String appid = wxPayConfig.getAppId();

            payInfo.put("appId", appid);
            payInfo.put("timeStamp", timestamp);
            payInfo.put("nonceStr", nonceStr);
            payInfo.put("package", "prepay_id=" + prepayId);
            payInfo.put("signType", "RSA");

            String beforeSign = String.format("%s\n%s\n%s\n%s\n", appid, timestamp, nonceStr, "prepay_id=" + prepayId);
            payInfo.put("paySign", SignUtils.sign(beforeSign, PemUtils.loadPrivateKey(new FileInputStream(wxPayConfig.getPrivateKeyPath()))));
            // 签名以后在增加prepayId参数
            payInfo.put("prepayId", prepayId);
            return JSON.toJSONString(payInfo);
        } catch (Exception e) {
            throw (e instanceof WxPayException) ? (WxPayException) e : new WxPayException(e.getMessage(), e);
        }
    }

}
