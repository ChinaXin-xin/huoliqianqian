package xin.h5.zfb.service.async;

import java.util.Map;

public interface AsyncPayDealTaskService {

	/**
	 * 处理支付宝异步回调结果
	 * @param params
	 * @param pid 
	 */
	void dealAliPayAsynchronous(Map<String, String> params, String pid);

	
	/**
	 * 处理微信异步回调结果
	 * @param payResultMap
	 */
	void dealWechatPayAsynchronous(Map<String, Object> payResultMap);
	
}
