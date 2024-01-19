package xin.weixin.service.orderForm.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.common.domain.CommonPrefix;
import xin.common.domain.CommonalityQuery;
import xin.common.domain.User;
import xin.common.exception.MyCustomException;
import xin.common.utils.RedisCache;
import xin.h5.zfb.utils.snowflake.IdGeneratorSnowflake;
import xin.weixin.domain.orderForm.OrderFormList;
import xin.weixin.domain.orderForm.OrderFormSub;
import xin.weixin.domain.shoppingTrolley.ShoppingTrolley;
import xin.weixin.mapper.orderForm.OrderFormMapper;
import xin.weixin.mapper.shoppingTrolley.ShoppingTrolleyMapper;
import xin.weixin.pay.wxPayUtils.WxPay;
import xin.weixin.service.orderForm.OrderFormService;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;
import xin.weixinBackground.domain.commodityDetails.CommoditySpecification;
import xin.weixinBackground.mapper.commodityDetails.CommodityDetailsMapper;
import xin.weixinBackground.mapper.commodityDetails.CommoditySpecificationMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderFormServiceImpl implements OrderFormService {

    @Autowired
    CommoditySpecificationMapper commoditySpecificationMapper;

    @Autowired
    CommodityDetailsMapper commodityDetailsMapper;

    @Autowired
    OrderFormMapper orderFormMapper;

    @Autowired
    IdGeneratorSnowflake idGeneratorSnowflake;

    @Autowired
    RedisCache redisCache;

    @Autowired
    ShoppingTrolleyMapper shoppingTrolleyMapper;

    @Autowired
    WxPay wxPay;

    // 订单超时时间，单位分钟，
    public static final Integer orderFormOvertime = 32;

    /**
     * 用户下单
     *
     * @param orderFormList 订单信息
     * @return 微信小程序的支付密钥
     */
    @Override
    @Transactional
    public ResponseResult placeAnOrder(OrderFormList orderFormList, HttpServletRequest request) {
        orderFormList.setOrderFormId(CommonPrefix.wxOrderFormPrefix_ + idGeneratorSnowflake.snowflakeId());
        return payOrderForm(orderFormList, request);
    }

    /**
     * 根据订单号进行支付
     *
     * @return
     */
    @Override
    public ResponseResult payByOrderFormId(String orderFormId, HttpServletRequest request) {

        // 从缓存中取出订单信息
        JSONObject jsonObject = redisCache.getCacheObject(orderFormId);
        OrderFormList orderFormList = jsonObject.toJavaObject(OrderFormList.class);

        // 进行微信支付
        JSONObject payKey = wxPay.pay(orderFormList, request);

        return new ResponseResult(200, "创建订单成功！", payKey);
    }

    public ResponseResult payOrderForm(OrderFormList orderFormList, HttpServletRequest request) {

        if (orderFormList.getShippingAddressId() == null) {
            return new ResponseResult(400, "未选择地址信息！");
        }

        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        // 设置订单所属用户id
        orderFormList.setUserId(curUser.getId());

        orderFormList.setCreateTIme(new Date());
        List<OrderFormSub> orderFormsSubList = orderFormList.getOrderFormsSubList();


        for (OrderFormSub orderFormSub : orderFormsSubList) {

            // 提交订单之后，删除购物车中对应的信息
            QueryWrapper<ShoppingTrolley> qw = new QueryWrapper<>();
            qw.eq("user_id", curUser.getId());
            qw.eq("specification_id", orderFormSub.getSpecificationId());
            shoppingTrolleyMapper.delete(qw);

            // 设置邀请人邀请码，根据邀请码来设置
            orderFormSub.setShareCode(orderFormList.getShareCode());

            // 设置收货地址
            orderFormSub.setShippingAddressId(orderFormList.getShippingAddressId());

            if (orderFormSub.getSpecificationId() == null || orderFormSub.getNumber() == null) {
                log.error("用户{}，创建订单时：请求参数错误！", orderFormSub.getUserId());
            }

            orderFormSub.setCreateTime(new Date());
            orderFormSub.setUserId(curUser.getId());
            orderFormSub.setStatus(0);

            // 查询仓库中对对应商品的信息
            CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectById(orderFormSub.getSpecificationId());
            if (commoditySpecification == null) {
                throw new MyCustomException(400, "未找到对应规格！");
            }

            if (commoditySpecification.getStock() < orderFormSub.getNumber()) {
                throw new MyCustomException(400, "库存不足，实际库存:" + commoditySpecification.getStock());
            }

            // 库存中减去对应的数量，如果取消支付或者超时再加上库存
            commoditySpecification.setStock(commoditySpecification.getStock() - orderFormSub.getNumber());

            // 设置订单号
            orderFormSub.setOrderFormId(orderFormList.getOrderFormId());

            // 订单写入数据库，并更新库存
            try {
                orderFormSub.setId(null);
                orderFormMapper.insert(orderFormSub);
            } catch (Exception e) {
                log.error("用户{}，创建订单时：地址或者商品规格不存在等！", orderFormSub.getUserId());
                throw new MyCustomException(400, String.format("用户%d，创建订单时：地址或者商品规格不存在等！", orderFormSub.getUserId()) + orderFormSub);
            }

            commoditySpecificationMapper.updateById(commoditySpecification);
            log.info("用户{}，创建订单号为：{}", orderFormSub.getUserId(), orderFormSub.getOrderFormId());
        }

        // 添加进入订单缓存
        redisCache.setCacheObject(orderFormList.getOrderFormId(), orderFormList, orderFormOvertime, TimeUnit.MINUTES);

        // 进行微信支付
        JSONObject payKey = wxPay.pay(orderFormList, request);

        return new ResponseResult(200, "创建订单成功！", payKey);
    }


    @Scheduled(fixedRate = 60000)
    public void examineOrderOvertime() {
        log.info("wx小程序订单超时检查执行成功");
        Collection<String> keys = redisCache.keys(CommonPrefix.wxOrderFormPrefix_ + "*");
        for (String key : keys) {
            JSONObject jsonObject = redisCache.getCacheObject(key);
            OrderFormList orderFormList = jsonObject.toJavaObject(OrderFormList.class);
            Date createTime = orderFormList.getCreateTIme();

            Calendar calendar = Calendar.getInstance(); // 获取订单创建时间
            calendar.setTime(createTime);
            calendar.add(Calendar.MINUTE, 30); // 将订单创建时间向后增加30分钟
            Date thirtyMinutesAfterCreateTime = calendar.getTime();

            if (thirtyMinutesAfterCreateTime.before(new Date())) {
                // 如果订单创建时间加30分钟后的时间早于当前时间，销毁订单
                cancellationOfOrder(orderFormList.getOrderFormId(), false);
            }
        }
    }


    /**
     * 取消订单，用户取消支付或者订单超时时调用
     *
     * @param orderFormId 订单编号
     * @param status      true为支付取消（然后设置为未付款），false为订单超时取消
     * @return
     */
    @Override
    public ResponseResult cancellationOfOrder(String orderFormId, boolean status) {

        // 从缓存中取出订单信息
        JSONObject jsonObject = redisCache.getCacheObject(orderFormId);
        OrderFormList orderFormList = jsonObject.toJavaObject(OrderFormList.class);

        for (OrderFormSub orderForm : orderFormList.getOrderFormsSubList()) {
            if (orderForm == null || orderForm.getSpecificationId() == null || orderForm.getNumber() == null) {
                log.error("取消订单错误，订单号：" + orderFormId + "不存在");
                return new ResponseResult(400, "请求参数错误！");
            }

            // 设置订单的状态
            orderForm.setStatus(status ? 0 : 4);

            // 删除缓存中的订单信息
            redisCache.deleteObject(orderFormId);

            // 更新数据库中的订单信息
            orderFormMapper.updateById(orderForm);

            // 查询仓库中对对应商品的信息
            CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectById(orderForm.getSpecificationId());
            if (commoditySpecification == null) {
                log.info("订单取消时找不到：用户{}，订单号为：{} 对应的商品规格", orderForm.getUserId(), orderForm.getOrderFormId());
            }

            // 把订单占用的库存数量归还到库存
            commoditySpecification.setStock(commoditySpecification.getStock() + orderForm.getNumber());
            commoditySpecificationMapper.updateById(commoditySpecification);

            if (status) {
                log.info("用户{}，支取取消，订单号为：{}", orderForm.getUserId(), orderForm.getOrderFormId());
            } else {
                log.info("用户{}，超时取消，订单号为：{}", orderForm.getUserId(), orderForm.getOrderFormId());
            }
        }
        return new ResponseResult<>(200, "订单取消成功！");
    }

    /**
     * 支付成功后的操作，商品添加到用户下
     *
     * @param orderFormId
     * @return
     */
    @Override
    @Transactional
    public ResponseResult paySucceed(String orderFormId) {
        // 从缓存中取出订单信息
        JSONObject jsonObject = redisCache.getCacheObject(orderFormId);
        OrderFormList orderFormList = jsonObject.toJavaObject(OrderFormList.class);

        List<OrderFormSub> orderFormsSubList = orderFormList.getOrderFormsSubList();
        for (OrderFormSub orderForm : orderFormsSubList) {
            if (orderForm == null || orderForm.getSpecificationId() == null || orderForm.getNumber() == null) {
                log.error("支付成功成功后查询订单错误，订单号：" + orderFormId + "不存在");
                //return new ResponseResult(500, "之后成功后查询订单错误，订单号：" + orderFormId + "不存在");
            }

            commoditySpecificationMapper.updateMarket(orderForm.getSpecificationId(), orderForm.getNumber());

            // 设置状态，订单，已完成
            orderForm.setStatus(1);

            // 更新数据库中的订单信息
            orderFormMapper.updateById(orderForm);

            log.info("用户{}，支付成功，订单号为：{} 待发货", orderForm.getUserId(), orderForm.getOrderFormId());
        }

        // 删除缓存中的订单信息
        redisCache.deleteObject(orderFormId);

        return new ResponseResult<>(200, "订单支付成功！");
    }

    /**
     * 查询该用户的查询待付款
     */
    public ResponseResult<CommonalityQuery<OrderFormSub>> queryPaymentOnBehalfOfOthers(CommonalityQuery<OrderFormSub> query) {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        ArrayList<OrderFormList> resultList = new ArrayList<>();

        Collection<String> keys = redisCache.keys(CommonPrefix.wxOrderFormPrefix_ + "*");

        for (String key : keys) {
            JSONObject jsonObject = redisCache.getCacheObject(key);
            OrderFormList orderFormList = jsonObject.toJavaObject(OrderFormList.class);
            if (orderFormList.getUserId().equals(curUser.getId())) {
                for (int i = 0, size = orderFormList.getOrderFormsSubList().size(); i < size; i++) {
                    OrderFormSub orderFormSub = orderFormMapper.selectById(orderFormList.getOrderFormsSubList().get(i).getId());

                    // 根据商品编码查询商品信息的id
                    Integer detailsId = commoditySpecificationMapper.selectByIdToDetailsId(orderFormSub.getSpecificationId());
                    CommodityDetails commodityDetails = commodityDetailsMapper.selectById(detailsId);

                    // 根据商品详情id，查询对应规格
                    commodityDetails.setCommoditySpecificationList(commoditySpecificationMapper.selectByCommodityDetailsIdAndSpecificationId(commodityDetails.getId(), orderFormSub.getSpecificationId()));

                    orderFormSub.setCommodityDetails(commodityDetails);
                    orderFormList.getOrderFormsSubList().set(i, orderFormSub);
                }
                Collections.sort(orderFormList.getOrderFormsSubList(), new Comparator<OrderFormSub>() {
                    @Override
                    public int compare(OrderFormSub o1, OrderFormSub o2) {
                        // 将o2和o1的位置对调来实现反向排序
                        return o2.getId().compareTo(o1.getId());
                    }
                });
                resultList.add(orderFormList);
            }
        }

        List<OrderFormSub> subResultList = new ArrayList<>();
        for (OrderFormList obj : resultList) {
            subResultList.addAll(obj.getOrderFormsSubList());
        }

        int pageNumber = query.getPageNumber(); // 当前页码
        int quantity = query.getQuantity(); // 每页显示的记录数
        int totalSize = subResultList.size(); // 总记录数

        // 计算最大页数
        int maxPageNumber = (totalSize + quantity - 1) / quantity;

        // 检查请求的页码是否超过最大页数
        if (pageNumber > maxPageNumber) {
            // 如果超过了，返回空的结果集
            query.setResultList(new ArrayList<>());
        } else {
            // 计算当前页的起始索引和结束索引
            int startIndex = (pageNumber - 1) * quantity;
            int endIndex = Math.min(startIndex + quantity, totalSize);

            // 获取当前页的子列表
            List<OrderFormSub> pagedSubResultList = subResultList.subList(startIndex, endIndex);

            // 设置分页后的结果和数量
            query.setResultList(pagedSubResultList);
        }

        query.setCount((long) subResultList.size());
        // 返回响应结果
        return new ResponseResult<>(200, "查询成功！", query);
    }

    /**
     * 全部订单
     * @param query
     * @return
     */
    @Override
    public ResponseResult<CommonalityQuery<OrderFormSub>> queryAllOrderForm(CommonalityQuery<OrderFormSub> query) {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Page<OrderFormSub> page = new Page<>(query.getPageNumber(), query.getQuantity());
        QueryWrapper<OrderFormSub> qw = new QueryWrapper<>();
        qw.eq("user_id", curUser.getId());
        qw.orderByDesc("id");
        qw.notIn("status", 3, 4);
        Page<OrderFormSub> resultList = orderFormMapper.selectPage(page, qw);
        List<OrderFormSub> records = resultList.getRecords();

        for (int i = 0, size = records.size(); i < size; i++) {
            OrderFormSub orderFormSub = records.get(i);

            // 根据商品编码查询商品信息的id
            Integer detailsId = commoditySpecificationMapper.selectByIdToDetailsId(orderFormSub.getSpecificationId());
            CommodityDetails commodityDetails = commodityDetailsMapper.selectById(detailsId);

            // 根据商品详情id，查询对应规格
            commodityDetails.setCommoditySpecificationList(commoditySpecificationMapper.selectByCommodityDetailsIdAndSpecificationId(commodityDetails.getId(), orderFormSub.getSpecificationId()));

            orderFormSub.setCommodityDetails(commodityDetails);
        }

        Collections.sort(records, new Comparator<OrderFormSub>() {
            @Override
            public int compare(OrderFormSub o1, OrderFormSub o2) {
                // 将o2和o1的位置对调来实现反向排序
                return o2.getId().compareTo(o1.getId());
            }
        });


        query.setResultList(records);
        query.setCount(resultList.getTotal());
        return new ResponseResult<>(200, "查询成功！", query);
    }

    /**
     * 根据订单装填，查询订单
     * 单状态 0：未完成  1：已完成  2：进行中  3：支付取消  4：超时取消  5：已退款  6：退款中
     *
     * @param query
     * @param statusList
     * @return
     */
    @Override
    public ResponseResult<CommonalityQuery<OrderFormSub>> selectOrderFormStatus(CommonalityQuery<OrderFormSub> query, int... statusList) {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Page<OrderFormSub> page = new Page<>(query.getPageNumber(), query.getQuantity());
        QueryWrapper<OrderFormSub> qw = new QueryWrapper<>();
        qw.eq("user_id", curUser.getId());
        qw.orderByDesc("id");
        qw.and(wrapper -> {
            for (int status : statusList) {
                wrapper.or().eq("status", status);
            }
        });
        Page<OrderFormSub> resultList = orderFormMapper.selectPage(page, qw);
        List<OrderFormSub> records = resultList.getRecords();

        for (int i = 0, size = records.size(); i < size; i++) {
            OrderFormSub orderFormSub = records.get(i);

            // 根据商品编码查询商品信息的id
            Integer detailsId = commoditySpecificationMapper.selectByIdToDetailsId(orderFormSub.getSpecificationId());
            CommodityDetails commodityDetails = commodityDetailsMapper.selectById(detailsId);

            // 根据商品详情id，查询对应规格
            commodityDetails.setCommoditySpecificationList(commoditySpecificationMapper.selectByCommodityDetailsIdAndSpecificationId(commodityDetails.getId(), orderFormSub.getSpecificationId()));

            orderFormSub.setCommodityDetails(commodityDetails);
        }
        query.setResultList(records);
        query.setCount(resultList.getTotal());

        return new ResponseResult<>(200, "查询成功！", query);
    }

    /**
     * 查询已完成
     */
    @Override
    public ResponseResult<CommonalityQuery<OrderFormSub>> queryHaveFinishOrderForm(CommonalityQuery<OrderFormSub> query) {
        return selectOrderFormStatus(query, 1, 5);
    }

    /**
     * 查询售后订单
     */
    @Override
    public ResponseResult<CommonalityQuery<OrderFormSub>> afterSale(CommonalityQuery<OrderFormSub> query) {
        return selectOrderFormStatus(query, 6);
    }
}
