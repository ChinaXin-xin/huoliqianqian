package xin.h5.zfb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * h5 用户订单表
 */
@Data
@TableName("sys_h5_order")
public class SysH5Order {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("sys_h5_goods_id")
    private Integer sysH5GoodsId;

    @TableField("sys_user_id")
    private Long sysUserId;
}
