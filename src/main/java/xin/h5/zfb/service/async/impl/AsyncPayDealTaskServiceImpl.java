package xin.h5.zfb.service.async.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import xin.h5.zfb.service.async.AsyncPayDealTaskService;

@Service
@Async
public class AsyncPayDealTaskServiceImpl implements AsyncPayDealTaskService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncPayDealTaskServiceImpl.class);
	
	@Autowired
	PlatformTransactionManager dataSourceTransactionManager;
	@Autowired
	TransactionDefinition transactionDefinition;
	
	/**
	 * 处理支付宝异步回调结果
	 */
	@Override
	public void dealAliPayAsynchronous(Map<String, String> params, String pid) {
		TransactionStatus transactionStatus = null;
		String out_trade_no = "";
		try{
			/*开启事务*/
			transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
			
			out_trade_no = params.get("out_trade_no").toString();//商户订单号
			String trade_status = params.get("trade_status").toString();//交易状态
            String total_amount = params.get("total_amount").toString();//订单金额
			if ("TRADE_SUCCESS".equals(trade_status)) {
				LOGGER.info("支付宝的支付状态为TRADE_SUCCESS，支付成功，处理业务逻辑啦=====");
				//=============================这里开始处理你的业务逻辑，业务逻辑处理主要遵循以下逻辑过程==================================
				//（1）根据商户订单号out_trade_no查询你的支付宝订单，查询订单是否存在。需要注意的是：像是商城多个业务订单批次合并一次性支付的场景，支付宝订单和商城订单是一对多的关系
				//（2）校验支付宝回调过来的金额total_amount与你调取存储的支付宝订单是否一致
				//（3）校验pid和回调过来的seller_id是否一致
				//（4）更新商户订单号out_trade_no更新支付宝订单的支付状态，注意幂等更新去重，不要重复更新了
				//（5）根据商户订单号out_trade_no关联到系统商城业务订单，属于一对多的关系，更新商城订单状态（待付款==》待发货），注意幂等更新去重，不要重复更新了
				//（6）若使用了延迟任务（超时未支付）记得从延迟队列中移除这些商城订单信息；若使用了redis，记得清除缓存
				LOGGER.info("支付宝支付回调信息处理成功");
	        }else {
	        	LOGGER.info("支付宝的支付状态为"+trade_status+"，支付失败啦====="+params.toString());
	        }
			dataSourceTransactionManager.commit(transactionStatus);
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("AsyncTaskServiceImpl -- dealAliPayRechargeAsynchronous方法处理异常：" + out_trade_no );
			dataSourceTransactionManager.rollback(transactionStatus);
		}
	}


	/**
	 * 余额充值：处理微信异步回调结果
	 */
	@Override
	public void dealWechatPayAsynchronous(Map<String, Object> payResultMap) {
		TransactionStatus transactionStatus = null;
		String out_trade_no = "";
		try{
			/*开启事务*/
			transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
			
			String result_code = payResultMap.get("result_code").toString();//支付结果
			String total_fee = payResultMap.get("total_fee").toString();//支付费用
			out_trade_no = payResultMap.get("out_trade_no").toString();//商户订单号
			if("SUCCESS".equals(result_code)) {
				LOGGER.info("微信H5的支付状态为SUCCESS，支付成功，处理业务逻辑啦=====");
				//=============================这里开始处理你的业务逻辑，业务逻辑处理主要遵循以下逻辑过程==================================
				//（1）根据商户订单号out_trade_no查询你的支付宝订单，查询订单是否存在。需要注意的是：像是商城多个业务订单批次合并一次性支付的场景，支付宝订单和商城订单是一对多的关系
				//（2）校验支付宝回调过来的金额total_fee与你调取存储的支付宝订单是否一致
				//（3）更新商户订单号out_trade_no更新支付宝订单的支付状态，注意幂等更新去重，不要重复更新了
				//（4）根据商户订单号out_trade_no关联到系统商城业务订单，属于一对多的关系，更新商城订单状态（待付款==》待发货），注意幂等更新去重，不要重复更新了
				//（5）若使用了延迟任务（超时未支付）记得从延迟队列中移除这些商城订单信息；若使用了redis，记得清除缓存
				
				LOGGER.info("微信H5支付回调信息处理成功");
			}else {
				LOGGER.info("微信H5的支付状态为"+result_code+"，支付失败啦====="+payResultMap.toString());
			}
			dataSourceTransactionManager.commit(transactionStatus);
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("AsyncTaskServiceImpl -- dealWechatPayOrderAsynchronous方法处理异常：");
			dataSourceTransactionManager.rollback(transactionStatus);
		}
	}

}
