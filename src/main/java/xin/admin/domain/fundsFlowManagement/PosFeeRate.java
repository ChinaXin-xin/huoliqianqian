package xin.admin.domain.fundsFlowManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 单个pos无感扣费的信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pos_fee_rate")
public class PosFeeRate {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("sys_fee_rate_id")
    private Integer sysFeeRateId;

    @TableField("pos_id")
    private Long posId; // 使用 Long 类型来匹配 UNSIGNED 属性

    private String feeRate;
}
