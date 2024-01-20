package xin.admin.domain.serviceCharge;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * pos机交易每笔服务费，公共的
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_pos_deal_service_charge")
public class SysPosDealServiceCharge {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 描述
    @TableField("name")
    private String name;

    // 结束区间
    @TableField("start_interval")
    private Integer startInterval;

    // 开始区间
    @TableField("end_interval")
    private Integer endInterval;

    // 每笔手续费
    @TableField("fee_per_transaction")
    private Float feePerTransaction;
}
