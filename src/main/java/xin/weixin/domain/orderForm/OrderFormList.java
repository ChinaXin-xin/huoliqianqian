package xin.weixin.domain.orderForm;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 一次提交的订单，这与OrderFormSub的区别是：
 *      这是对应多个商品
 *      OrderFormSub是订单是只对应一件商品
 *      这里的数据不存入mysql，只存入redis，有效设置手动设置
 */
@Data
public class OrderFormList {

    // 所属用户id
    private Long userId;

    // 订单编号
    private String orderFormId;

    // 子订单，一个商品对应一个订单
    private List<OrderFormSub> orderFormsSubList;

    // 收货地址
    private Integer shippingAddressId;

    // 订单创建的时间
    private Date createTIme;

    // 分享人的邀请码
    private String shareCode;
}
