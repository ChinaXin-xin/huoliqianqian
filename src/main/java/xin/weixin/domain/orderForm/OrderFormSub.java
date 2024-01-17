package xin.weixin.domain.orderForm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;

import java.util.Date;

/**
 * 每个商品的订单，这与OrderFormList的区别是：
 *      这是对应一份商品的
 *      OrderFormList是订单可以里面包含多个OrderFormSub
 *      这里的数据不存入redis，只存入mysql
 */
@Data
@TableName("wx_order_form")
public class OrderFormSub {

    // id
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 订单号
    private String orderFormId;

    // 下单的用户id
    private Long userId;

    // 收货地址
    private Integer shippingAddressId;

    // 商品规格编码
    private Integer specificationId;

    // 用户下单的数量
    private Integer number;

    // 订单状态 0：未完成  1：已完成  2：待发货  3：支付取消  4：超时取消  5：已退款  6：退款中  7：商家已发货
    private Integer status = 0;

    // 订单是否发货
    private Boolean isDeliverGoods = false;

    // 分享人的邀请码
    private String shareCode;

    // 订单的创建时间
    private Date createTime;

    @TableField(exist = false)
    private CommodityDetails commodityDetails;
}
