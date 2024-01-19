package xin.weixin.pay.wxPayUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import xin.admin.domain.LoginUser;
import xin.common.domain.User;
import xin.weixin.domain.orderForm.OrderFormList;
import xin.weixin.domain.orderForm.OrderFormSub;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;
import xin.weixinBackground.domain.commodityDetails.CommoditySpecification;
import xin.weixinBackground.mapper.commodityDetails.CommodityDetailsMapper;
import xin.weixinBackground.mapper.commodityDetails.CommoditySpecificationMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 微信支付，小程序支付
 */
@Component
@Slf4j
public class WxPay {

    @Value("${wxpay.wxKeyPath}")
    private String wxKeyPath;

    @Value("${wxpay.wxPrivateCertPath}")
    private String wxPrivateCertPath;

    @Value("${wxpay.wxPrivateKeyPath}")
    private String wxPrivateKeyPath;

    @Value("${wxpay.mchId}")
    private String wxMchId;

    @Value("${wxpay.appId}")
    private String wxAppId;

    @Value("${wxpay.mchKey}")
    private String wxMchKey;

    @Value("${wxpay.apiV3Key}")
    private String wxApiV3Key;

    @Value("${wxpay.certSerialNo}")
    private String wxCertSerialNo;

    @Autowired
    private CommoditySpecificationMapper commoditySpecificationMapper;

    @Autowired
    private CommodityDetailsMapper commodityDetailsMapper;

    public JSONObject pay(OrderFormList orderFormList, HttpServletRequest request) {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(wxMchId);
        wxPayConfig.setAppId(wxAppId);
        wxPayConfig.setMchKey(wxMchKey);
        wxPayConfig.setApiV3Key(wxApiV3Key);
        wxPayConfig.setCertSerialNo(wxCertSerialNo);
        try {
            // 如果找不到证书
            wxPayConfig.setKeyPath(wxKeyPath);
            wxPayConfig.setPrivateCertPath(wxPrivateCertPath);
            wxPayConfig.setPrivateKeyPath(wxPrivateKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // 小程序支付
        wxPayConfig.setTradeType(WxPayConstants.TradeType.JSAPI);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig); //微信配置信息

        // 官方接口文档地址- https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_1_1.shtml
        String url = "/v3/pay/transactions/jsapi";
        try {
            JSONObject param = param(orderFormList, request);
            if (param == null) {
                return null;
            }
            JSONObject jsonObject = WxpayV3Util.unifiedOrderV3(url, param, wxPayService);
            System.out.println("支付返回：" + jsonObject);
            return jsonObject;
        } catch (WxPayException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject param(OrderFormList orderFormList, HttpServletRequest request) {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        // 获取用户 IP 地址
        String curUserIp = request.getRemoteAddr();


        // 商品订单号
        String payOrderId = orderFormList.getOrderFormId();
        StringBuilder description = new StringBuilder();
        int total = 0;

        List<OrderFormSub> subs = orderFormList.getOrderFormsSubList();
        for (int i = 0; i < subs.size(); i++) {
            OrderFormSub sub = subs.get(i);
            CommoditySpecification cs = commoditySpecificationMapper.selectById(sub.getSpecificationId());
            if (cs == null) {
                log.error("用户：{}，创建订单时，规格号不存在：{}", curUser.getId(), sub.getSpecificationId());
                return null;
            }
            if (i == 0) {
                CommodityDetails commodityDetails = commodityDetailsMapper.selectById(cs.getCommodityDetailsId());
                description.append(commodityDetails.getDetails());
                description.append("等，");
                description.append(subs.size());
                description.append(" 件商品");
            }
            total += cs.getSinglePrice() * 100 * sub.getNumber();
        }


        // 微信统一下单请求对象
        JSONObject reqJSON = new JSONObject();
        reqJSON.put("out_trade_no", payOrderId);
        reqJSON.put("description", description.toString());

        reqJSON.put("notify_url", "https://www.rrthb.com/pro/pay/notify");
        JSONObject amount = new JSONObject();
        amount.put("total", total);
        amount.put("currency", "CNY");

        reqJSON.put("amount", amount);

        JSONObject payer = new JSONObject();
        payer.put("openid", curUser.getOpenId());

        JSONObject sceneInfo = new JSONObject();
        sceneInfo.put("payer_client_ip", curUserIp);

        reqJSON.put("scene_info", sceneInfo);
        reqJSON.put("appid", wxAppId);
        reqJSON.put("mchid", wxMchId);
        reqJSON.put("payer", payer);

        return reqJSON;
    }
}
