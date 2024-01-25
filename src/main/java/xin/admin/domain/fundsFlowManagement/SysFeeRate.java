package xin.admin.domain.fundsFlowManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 统一费用表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_fee_rate")
public class SysFeeRate {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 费率名称
    private String feeRateName;

    // 费率
    private String feeRate;
}
