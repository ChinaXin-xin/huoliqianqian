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
 * 设置pos机对应每个交易区间的手续费
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_some_pos_deal_service_charge")
public class SysSomePosDealServiceCharge {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // pos机的id
    @TableField("pos_id")
    private Integer posId;


    // 金额区间与对应的费率表的id
    @TableField("spdsc_id")
    private Integer spdscId;

    // 对应服务器
    private Float money;
}
