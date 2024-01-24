package xin.admin.domain.fundsFlowManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 扣费记录实体类
 */
@Data
@TableName("sys_fee_deduction_record")
public class SysFeeDeductionRecord {

    /**
     * 唯一标识符，自增长
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 交易时间 -
     */
    private Date transactionTime;

    // 用于对transactionTime查询，开始时间与结束时间
    @TableField(exist = false)
    private Date startTransactionTime;
    @TableField(exist = false)
    private Date endTransactionTime;

    /**
     * 交易金额 -
     */
    private String amount;

    /**
     * 交易类型 -
     */
    private String type;

    /**
     * 终端sn -
     */
    private String pos;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 流水号 -
     */
    private String serialNumber;

    /**
     * 操作号 -
     */
    private String operatorNumber;

    /**
     * 交易状态 -
     */
    private String status;
}
