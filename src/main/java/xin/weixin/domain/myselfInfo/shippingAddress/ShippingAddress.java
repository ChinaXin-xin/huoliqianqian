package xin.weixin.domain.myselfInfo.shippingAddress;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户收货地址
 */
@Data
@TableName("wx_shipping_address")
public class ShippingAddress {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Long userId;

    private String phone;

    private String name;

    // 收货地址
    private String shippingAddress;

    // 详细地址
    private String detailsAddress;

    // 是否是默认地址，0不是，1是，前端让用这个，我也不知道为什么不用布尔类型
    private Integer isDefault = 0;
}
