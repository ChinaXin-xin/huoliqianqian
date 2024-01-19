package xin.weixin.service.orderForm;

import org.springframework.web.bind.annotation.RequestBody;
import xin.admin.domain.ResponseResult;
import xin.common.domain.CommonalityQuery;
import xin.weixin.domain.orderForm.OrderFormList;
import xin.weixin.domain.orderForm.OrderFormSub;

import javax.servlet.http.HttpServletRequest;

public interface OrderFormService {

    /**
     * 用户下单
     *
     * @param orderFormList
     * @return 返回订单号
     */
    ResponseResult placeAnOrder(OrderFormList orderFormList, HttpServletRequest request);

    /**
     * 根据订单号进行支付
     *
     * @param orderFormId
     * @return 返回订单号
     */
    ResponseResult payByOrderFormId(@RequestBody String orderFormId, HttpServletRequest request);

    /**
     * 取消订单，用户取消支付时调用，或者订单超时时调用
     *
     * @param orderFormId 订单编号
     * @param status      true为支付超时取消，false为订单超时取消
     * @return
     */
    ResponseResult cancellationOfOrder(String orderFormId, boolean status);

    /**
     * 支付成功后的操作，商品添加到用户下
     *
     * @param orderFormId
     * @return
     */
    ResponseResult paySucceed(String orderFormId);


    /**
     * 定时任务，每分钟执行一次
     */
    void examineOrderOvertime();

    /**
     * 查询该用户的查询待付款
     */
    ResponseResult<CommonalityQuery<OrderFormSub>> queryPaymentOnBehalfOfOthers(CommonalityQuery<OrderFormSub> query);

    /**
     * 查询该用户的全部的单一商品订单
     */
    ResponseResult<CommonalityQuery<OrderFormSub>> queryAllOrderForm(CommonalityQuery<OrderFormSub> query);

    /**
     * 根据订单装填，查询订单
     * 单状态 0：未完成  1：已完成  2：进行中  3：支付取消  4：超时取消  5：已退款  6：退款中
     *
     * @param query
     * @param statusList
     * @return
     */
    ResponseResult<CommonalityQuery<OrderFormSub>> selectOrderFormStatus(CommonalityQuery<OrderFormSub> query, int... statusList);

    /**
     * 查询已完成
     */
    ResponseResult<CommonalityQuery<OrderFormSub>> queryHaveFinishOrderForm(CommonalityQuery<OrderFormSub> query);

    /**
     * 查询售后订单
     */
    ResponseResult<CommonalityQuery<OrderFormSub>> afterSale(CommonalityQuery<OrderFormSub> query);
}
