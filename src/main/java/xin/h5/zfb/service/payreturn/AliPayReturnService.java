package xin.h5.zfb.service.payreturn;

import javax.servlet.http.HttpServletRequest;

import xin.h5.zfb.entity.R;


/**
 * 支付宝回调
 * @author Administrator
 *
 */
public interface AliPayReturnService {
	
	
	/**
	 * 支付宝同步支付回调结果处理
	 * @param request
	 * @return
	 */
	R aliPaySynchronizationResult(HttpServletRequest request);
	
	/**
	 * 支付宝异步支付回调结果处理
	 * @param request
	 * @return
	 */
	R aliPayAsynchronousResult(HttpServletRequest request);


}
