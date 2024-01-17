package xin.weixin.domain.shoppingTrolley;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;
import xin.weixinBackground.domain.commodityDetails.CommoditySpecification;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wx_shopping_trolley")
public class ShoppingTrolley {

    @TableId(type = IdType.AUTO)
    private Integer id; // 订单id

    @TableField("user_id")
    private Long userId; // 下单的用户id

    @TableField("specification_id")
    private Integer specificationId; // 商品规格编码

    // 商品与商品规格
    @TableField(exist = false)
    private CommodityDetails commodityDetails;

    @TableField(exist = false)
    private CommoditySpecification commoditySpecification;

    @TableField("number")
    private Integer number; // 用户下单的数量

    @TableField("create_time")
    private Date createTime; // 订单的创建时间
}
