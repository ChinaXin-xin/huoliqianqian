package xin.yunhuo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;

/**
 * 用户提现，转账记录
 */
@Data
@TableName(value = "sys_payment_record",autoResultMap = true)
public class PaymentRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 平台ID
    private String platformId = "20201221372355362021257216";
    // 商户订单号
    private String merOrderNo;
    // 收款人姓名
    private String inAcctName;
    // 收款人身份证号
    private String inCidno;
    // 收款人手机号
    private String inMobile;
    // 收款人银行卡号
    private String inAcctNo;
    // 打款金额
    private Long amount;
    // 打款备注
    private String remark;
    // 收款人地址
    private String cidAddress;
    // 发票编码
    private String invoiceCategory;
    // 发票编码对应任务ID
    private String laborTaskId;
    // 身份证正面图片
    private String id_card_front;
    // 身份证反面图片
    private String id_card_back;


    @TableField(typeHandler = FastjsonTypeHandler.class)
    // 提交的时候，平台相应的信息
    private YunhuoResponseMsg yunhuoResponseMsg;
}
