package xin.admin.domain.fundsFlowManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 机构交易流水通知内容对应的实体类，兼容之前php的
 * 对应原来服务器上的表：member_shanghu_order_zhongfu，现在改名为commercial_tenant_order_zf了
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("commercial_tenant_order_zf")
public class CommercialTenantOrderZF {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 020000	消费	加款
     * 020002	消费撤销	减款
     * 020003	消费冲正	减款
     * 020023	消费撤销冲正	加款
     * U20000	电子现金	加款
     * T20003	日结消费冲正	减款
     * T20000	日结消费	加款
     * 024100	预授权完成	加款
     * 024102	预授权完成撤销	减款
     * 024103	预授权完成冲正	减款
     * 024123	预授权完成撤销 冲正	加款
     * 020001	退货	减款
     */

    @TableField("tranCode")
    private String tranCode; // 交易码，根据这个判断他的交易类型

    @TableField("agentId")
    private String agentId; // 商户直属机构号

    @TableField("tranTime")
    private String tranTime; // 交易时间

    @TableField("cardNo")
    private String cardNo; // 卡号ji

    @TableField("traceNo")
    private String traceNo; // 凭证号

    @TableField("sysTraceNo")
    private String sysTraceNo; // 系统流水号

    @TableField("channelTraceNo")
    private String channelTraceNo; // 渠道凭证号

    @TableField("channelSerialNo")
    private String channelSerialNo; // 渠道流水号

    @TableField("channelRrn")
    private String channelRrn; // 渠道参考号

    @TableField("rrn")
    private String rrn; // 参考号

    @TableField("authCode")
    private String authCode; // 授权码

    @TableField("batchNo")
    private String batchNo; // 终端批次号

    @TableField("orderId")
    private String orderId; // 订单号

    @TableField("inputMode")
    private String inputMode; // 输入方式

    @TableField("cardType")
    private String cardType; // 卡类型

    @TableField("bankName")
    private String bankName; // 发卡行

    @TableField("merchantId")
    private String merchantId; // 商户号

    @TableField("merchLevel")
    private String merchLevel; // 商户类别

    @TableField("termId")
    private String termId; // 终端号

    @TableField("termSn")
    private String termSn; // 终端SN

    @TableField("termModel")
    private String termModel; // 终端型号

    @TableField("mobileNo")
    private String mobileNo; // 商户手机号

    @TableField("merchantName")
    private String merchantName; // 商户名称

    @TableField("amount")
    private BigDecimal amount; // 交易金额

    @TableField("o_amount")
    private BigDecimal oAmount; // 交易金额备用字段

    @TableField("settleAmount")
    private BigDecimal settleAmount; // 结算金额

    @TableField("feeType")
    private String feeType; // 手续费计算类型

    @TableField("settleDate")
    private String settleDate; // 清算日期

    @TableField("sysRespCode")
    private String sysRespCode; // 收单平台应答码

    @TableField("sysRespDesc")
    private String sysRespDesc; // 收单平台应答描述

    @TableField("createtime")
    private String createtime; // 写入平台时间

    @TableField("updatetime")
    private String updatetime; // 修改时间

    @TableField("is_chuli")
    private Boolean isChuli; // 订单状态

    @TableField("logno")
    private String logno; // 交易日期+参考号

    public Boolean getChuli() {
        if (this.isChuli == null)
            return false;
        return isChuli;
    }

    public String getLogno() {
        if (logno == null)
            return "";
        return logno;
    }
}
