package xin.h5.zfb.service.pay.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.common.domain.CommonPrefix;
import xin.common.domain.User;
import xin.common.utils.RedisCache;
import xin.h5.zfb.domain.SysH5Goods;
import xin.h5.zfb.entity.R;
import xin.h5.zfb.mapper.SysH5GoodsMapper;
import xin.h5.zfb.service.pay.AliPayService;
import xin.h5.zfb.utils.pay.alipay.AliPayUtil;
import xin.h5.zfb.utils.snowflake.IdGeneratorSnowflake;
import xin.h5.zfb.utils.string.StringUtil;
import xin.weixinBackground.mapper.commodityDetails.CommoditySpecificationMapper;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 支付
 *
 * @author Administrator
 */
@Service
public class AliPayServiceImpl implements AliPayService {



    @Autowired
    private RedisCache redisCache;

    private CommoditySpecificationMapper commoditySpecificationMapper;

    @Autowired
    private SysH5GoodsMapper sysH5GoodsMapper;

    @Autowired
    IdGeneratorSnowflake idGeneratorSnowflake;

    /**
     * alipay.trade.wap.pay：H5手机网站支付接口2.0（外部商户创建订单并支付），注意：total_amount单位是元
     */
    @Override
    public R alipayTradeWapPay(SysH5Goods sysH5Goods) {

        // 创建的订单号
        String orderFormId = CommonPrefix.h5OrderFormPrefix_ + idGeneratorSnowflake.snowflakeId();

        // 库存中对应的商品信息
        SysH5Goods warehouse = sysH5GoodsMapper.selectById(sysH5Goods.getId());

        // 如果要购买的商品不存在
        if (warehouse == null) {
            return R.error("该规格未找到！");
        }

        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        System.out.println("获取到用户id:" + curUser.getId());
        warehouse.setUserId(curUser.getId());

        // 订单进入缓存，缓存时间五分钟
        redisCache.setCacheObject(orderFormId, warehouse, 30, TimeUnit.MINUTES);

        String pay_result = AliPayUtil.alipayTradeWapPay(orderFormId, Float.toString(warehouse.getPrice()));
        System.out.println("订单号创建：" + orderFormId);

/*        // 前端需要的规格
        CommoditySpecification orderForm = (CommoditySpecification) map.get("commoditySpecification");
        if (orderForm == null) {
            return R.error("参数错误！");
        }

        // 查询库存中对应规格的商品信息
        CommoditySpecification warehouse = commoditySpecificationMapper.selectBySpecification(orderForm);
        if (warehouse == null) {
            return R.error("该规格未找到！");
        }

        // 如果用户购买的大于库存的数量
        if (orderForm.getStock() > warehouse.getStock()) {
            return R.error("购买数量大于实际数量，库存数为！" + warehouse.getStock());
        }

        // 用于所需要付的钱：用户所买的数量与商品的价格，
        float money = orderForm.getStock() * warehouse.getSinglePrice();

        // 转为两位小数，1.999，转为1.99
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);

        // 订单进入缓存，缓存时间五分钟
        redisCache.setCacheObject(orderFormId, orderForm, 30, TimeUnit.MINUTES);

        // 进行支付
        String pay_result = AliPayUtil.alipayTradeWapPay(orderFormId, df.format(money));
        System.out.println("订单创建：" + CommonPrefix.orderFormPrefix_ + orderFormId);

        */

        if (StringUtils.isNoneEmpty(pay_result)) {
            return R.ok("code_999999", "操作成功", pay_result);
        } else {
            return R.error("支付宝调取异常");
        }
    }


    /**
     * alipay.trade.page.pay：统一收单下单并支付页面接口（PC场景下单并支付），注意：total_amount单位是元
     */
    @Override
    public R alipayTradePagePay(Map<String, Object> map) {
        //===================这里你可以保存你的订单和支付宝订单信息（商户号和订单金额需要保存，支付宝异步回调时需要根据商户号处理业务逻辑）======================
        //（1）封装统一支付接口并调用
        String out_trade_no = StringUtil.getDateTimeAndRandomForID();//生成商户订单号
        String total_amount = "0.01";//交易金额
        String pay_result = AliPayUtil.alipayTradePagePay(out_trade_no, total_amount);
        if (StringUtils.isNoneEmpty(pay_result)) {
            return R.ok("code_999999", "操作成功", pay_result);
        } else {
            return R.error("支付宝调取异常");
        }
    }
}
