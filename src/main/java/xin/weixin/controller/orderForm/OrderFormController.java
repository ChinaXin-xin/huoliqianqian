package xin.weixin.controller.orderForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.common.domain.CommonalityQuery;
import xin.weixin.domain.orderForm.OrderFormList;
import xin.weixin.domain.orderForm.OrderFormSub;
import xin.weixin.service.orderForm.OrderFormService;

/**
 * 用户的下单请求处理
 */
@RestController
@RequestMapping("/pro/orderForm")
public class OrderFormController {

    @Autowired
    OrderFormService orderFormService;

    /**
     * 商品下单
     *
     * @return
     */
    @PostMapping("/placeAnOrder")
    public ResponseResult placeAnOrder(@RequestBody OrderFormList orderFormList) {
        return orderFormService.placeAnOrder(orderFormList);
    }

    /**
     * 查询该用户待付款
     */
    @PostMapping("/queryPaymentOnBehalfOfOthers")
    public ResponseResult<CommonalityQuery<OrderFormSub>> queryPaymentOnBehalfOfOthers(@RequestBody CommonalityQuery<OrderFormSub> query) {
        return orderFormService.queryPaymentOnBehalfOfOthers(query);
    }

    /**
     * 查询该用户的全部的单一商品订单
     */
    @PostMapping("/queryAllOrderForm")
    public ResponseResult<CommonalityQuery<OrderFormSub>> queryAllOrderForm(@RequestBody CommonalityQuery<OrderFormSub> query) {
        return orderFormService.queryAllOrderForm(query);
    }

    /**
     * 查询已完成
     */
    @PostMapping("/queryHaveFinishOrderForm")
    public ResponseResult<CommonalityQuery<OrderFormSub>> queryHaveFinishOrderForm(@RequestBody CommonalityQuery<OrderFormSub> query) {
        return orderFormService.queryHaveFinishOrderForm(query);
    }


    /**
     * 查询售后订单
     */
    @PostMapping("/afterSale")
    public ResponseResult<CommonalityQuery<OrderFormSub>> afterSale(@RequestBody CommonalityQuery<OrderFormSub> query) {
        return orderFormService.afterSale(query);
    }
}
