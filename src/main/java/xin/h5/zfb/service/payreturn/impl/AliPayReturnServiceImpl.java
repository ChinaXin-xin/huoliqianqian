package xin.h5.zfb.service.payreturn.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import xin.common.domain.CommonPrefix;
import xin.common.utils.RedisCache;
import xin.h5.zfb.config.properties.AliPayProperties;
import xin.h5.zfb.domain.SysH5Goods;
import xin.h5.zfb.domain.SysH5Order;
import xin.h5.zfb.entity.R;
import xin.h5.zfb.mapper.SysH5OrderMapper;
import xin.h5.zfb.service.async.AsyncPayDealTaskService;
import xin.h5.zfb.service.payreturn.AliPayReturnService;
import xin.h5.zfb.utils.string.RequestMap;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 支付宝回调
 *
 * @author Administrator
 */
@Service
public class AliPayReturnServiceImpl implements AliPayReturnService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliPayReturnServiceImpl.class);

    @Autowired
    private AliPayProperties aliPayProperties;
    @Autowired
    private AsyncPayDealTaskService asyncPayDealTaskService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysH5OrderMapper sysH5OrderMapper;

    /**
     * 支付宝同步支付回调结果处理
     */
    @Override
    public R aliPaySynchronizationResult(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        try {
            params = RequestMap.getParameterMap(request);
            System.out.println("支付宝的返回参数信息" + params.toString());

            // 假设 params 是一个 Map 类型，包含了交易号
            String outTradeNo = params.get("out_trade_no");

            // 完成订单
            purchasePay(outTradeNo);

            if (!AlipaySignature.rsaCheckV1(params, aliPayProperties.getAli_alipay_public_key(), aliPayProperties.getAli_charset(), aliPayProperties.getAli_sign_type())) {
                System.out.println("！！！验签失败！！！");
                return R.error("code_999277", "验签失败");
            }
            return R.ok("code_999999", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("AliPayReturnServiceImpl -- aliPaySynchronizationResultRecharge方法执行异常");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.error("code_999283", "支付宝同步通知处理异常");
        }
    }


    /**
     * 支付宝异步支付回调结果处理
     */
    @Override
    public R aliPayAsynchronousResult(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        try {
            params = RequestMap.getParameterMap(request);
            String outTradeNo = params.get("out_trade_no");
            System.out.println("异步通知  =======----" + outTradeNo);

            // 完成订单
            purchasePay(outTradeNo);

            if (!AlipaySignature.rsaCheckV1(params, aliPayProperties.getAli_alipay_public_key(), aliPayProperties.getAli_charset(), aliPayProperties.getAli_sign_type())) {
                System.out.println("！！！验签失败!!!");
                return R.error("code_999277", "验签失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("AliPayReturnServiceImpl -- aliPayAsynchronousResultRecharge方法执行异常");
            return R.error("code_999283", "支付宝异步通知处理异常");
        }
        if (params != null) {
            System.out.println("验签成功，处理支付结果!!!");
            asyncPayDealTaskService.dealAliPayAsynchronous(params, aliPayProperties.getAli_pid());
        }

        return R.ok("code_999999", "操作成功");
    }

    /**
     * 用户购买支付后的
     *
     * @param
     * @return
     */
    public void purchasePay(String outTradeNo) {

        JSONObject jsonObject = redisCache.getCacheObject(outTradeNo);
        SysH5Goods sysH5Goods = jsonObject.toJavaObject(SysH5Goods.class);

        System.out.println("----------------------");
        System.out.println(CommonPrefix.h5OrderFormPrefix_ + outTradeNo);
        System.out.println(sysH5Goods);

        SysH5Order sysH5Order = new SysH5Order();
        sysH5Order.setSysUserId(sysH5Goods.getUserId());
        sysH5Order.setSysH5GoodsId(sysH5Goods.getId());

        if (sysH5OrderMapper.insert(sysH5Order) > 0) {
            System.out.println("订单完成成成功！");
        } else {
            System.out.println("订单完成成失败！");
        }
        redisCache.deleteObject(CommonPrefix.h5OrderFormPrefix_ + outTradeNo);
    }
}
