package xin.admin.domain.fundsFlowManagement;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

// 中付pos机的服务器
@Data
public class ServiceCharge {
    // 创建用户ID
    private String createUserId;

    // 操作编号
    private String optNo;

    // 请求流水号 (唯一，不超过32位)
    private String traceNo;

    // 商户支付状态
    private String merchPayStatus;

    // 上次操作用户ID
    private String lastUserId;

    // 上次修改时间 (以数字格式表示)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private long lastModTime;

    // 终端ID
    private String termId;

    // POS服务费金额 (元) (如果传0代表不扣POS服务费)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal posCharge;

    // 渠道商户ID
    private String channelMerchId;

    // 商户支付结果
    private String merchPayResult;

    // ID
    private String id;

    // 终端SN序列号
    private String sn;

    // 操作时间 (以数字格式表示)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private long optTime;

    // 订单编号
    private String orderNo;

    // 代理商收款状态
    private String agentCollectStatus;

    // 费用类型
    private String feeType;

    // VIP会员服务费金额 (元) (如果传0代表不扣VIP会员费)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal vipCharge;

    // 商户ID
    private String merchId;

    // 创建时间 (以数字格式表示)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private long createTime;

    // 代理商收款结果
    private String agentCollectResult;

    // SIM服务费金额 (元) (如果传0代表不扣通信SIM费)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private BigDecimal simCharge;

    // 开通日期
    private String openDate;

    // 以下是在类定义之外提到的附加字段

    // 服务商编号
    private String agentId;

    // Token
    private String token;

    // 是否发送短信 (1发送, 0不发送) 注: 当前只允许传1发送
    private String smsSend;

    // 短信模板编号
    private String smsCode;

    // 数字签名
    private String sign;
}
