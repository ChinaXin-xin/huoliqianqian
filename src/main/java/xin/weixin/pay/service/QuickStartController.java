package xin.weixin.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.common.domain.User;
import xin.weixin.pay.utils.WxpayV3Util;

import java.io.IOException;

@RestController
@RequestMapping("/pro/test")
public class QuickStartController {

    @Value("${wxKeyPath}")
    private String wxKeyPath;

    @Value("${wxPrivateCertPath}")
    private String wxPrivateCertPath;

    @Value("${wxPrivateKeyPath}")
    private String wxPrivateKeyPath;

    @PostMapping("/pay")
    public ResponseResult pay() throws IOException {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId("1666131252");
        wxPayConfig.setAppId("wx2b213ad5e5c4c2a8");
        wxPayConfig.setMchKey("134679");
        wxPayConfig.setApiV3Key("abcde12345abcde12345abcde12345ab");
        wxPayConfig.setCertSerialNo("7761F8E867C2B0EC4931E46CD59A49F6B6C9AB27");

        wxPayConfig.setKeyPath(wxKeyPath);

        wxPayConfig.setPrivateCertPath(wxPrivateCertPath);

        wxPayConfig.setPrivateKeyPath(wxPrivateKeyPath);

        // 小程序支付
        wxPayConfig.setTradeType(WxPayConstants.TradeType.JSAPI);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig); //微信配置信息

        // 官方接口文档地址- https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_1.shtml
        String url = "/v3/pay/transactions/jsapi";
        try {
            JSONObject jsonObject = WxpayV3Util.unifiedOrderV3(url, param(), wxPayService);
            System.out.println("支付返回：" + jsonObject);
            return new ResponseResult(200, "查询成功！", jsonObject);
        } catch (WxPayException e) {
            e.printStackTrace();
        }
        return new ResponseResult(400, "异常");
    }

    public static void main(String[] args) throws IOException {
        QuickStartController quickStartController = new QuickStartController();
        quickStartController.pay();
    }

    private static JSONObject param() {

        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        String payOrderId = "12345678903423534539";

        // 微信统一下单请求对象
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("out_trade_no", payOrderId);
        reqJSON.put("description", "我是一个商品");
        // 订单失效时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE,示例值：2018-06-08T10:34:56+08:00
//        reqJSON.put("time_expire", String.format("%sT%s+08:00", DateUtil.format(orderPay.getExpiredTime(), DatePattern.NORM_DATE_FORMAT), DateUtil.format(orderPay.getExpiredTime(), DatePattern.NORM_TIME_FORMAT)));

        reqJSON.put("notify_url", "https://www.baidu.com");
        JSONObject amount = new JSONObject();
        amount.put("total", 888888);
        amount.put("currency", "CNY");
        reqJSON.put("amount", amount);

        JSONObject sceneInfo = new JSONObject();
        sceneInfo.put("payer_client_ip", "120.219.119.93");
        reqJSON.put("scene_info", sceneInfo);
        //System.out.println("用户openid:" + curUser.getOpenId());
        reqJSON.put("appid", "wx2b213ad5e5c4c2a8");
        reqJSON.put("mchid", "1666131252");
        JSONObject payer = new JSONObject();
        payer.put("openid", curUser.getOpenId());
        //payer.put("openid", "oQ_0z4wOUWvyn4FgeugXWq8mLTa0");
        reqJSON.put("payer", payer);

        return reqJSON;
    }
}
