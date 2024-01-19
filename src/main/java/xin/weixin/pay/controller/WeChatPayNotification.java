package xin.weixin.pay.controller;

import lombok.Data;

import lombok.Data;

import lombok.Data;

/**
 * WeChatPayNotification represents the notification data structure for WeChat Pay callbacks.
 */
@Data
public class WeChatPayNotification {
    private String mchid; // 商户号
    private String appid; // 应用ID
    private String outTradeNo; // 商户订单号
    private String transactionId; // 微信支付订单号
    private String tradeType; // 交易类型
    private String tradeState; // 交易状态
    private String tradeStateDesc; // 交易状态描述
    private String bankType; // 付款银行
    private String attach; // 附加数据
    private String successTime; // 支付完成时间
    private Payer payer; // 支付者信息
    private Amount amount; // 交易金额信息

    /**
     * Payer represents the payer's information in WeChat Pay.
     */
    @Data
    public static class Payer {
        private String openid; // 支付者的openid
    }

    /**
     * Amount represents the amount information in the WeChat Pay.
     */
    @Data
    public static class Amount {
        private Integer total; // 订单总金额
        private Integer payerTotal; // 用户支付金额
        private String currency; // 货币类型
        private String payerCurrency; // 用户支付的货币类型
    }
}

