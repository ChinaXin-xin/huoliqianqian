package xin.h5.zfb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * h5商品详情类
 */
@Data
@TableName("sys_h5_goods")
public class SysH5Goods {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Float price;
    private String name;

    @TableField(exist = false)
    private Long userId;
}
