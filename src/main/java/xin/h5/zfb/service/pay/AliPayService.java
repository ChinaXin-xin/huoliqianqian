package xin.h5.zfb.service.pay;

import java.util.Map;

import xin.h5.zfb.domain.SysH5Goods;
import xin.h5.zfb.entity.R;

public interface AliPayService {

	
	/**
	 * alipay.trade.wap.pay：H5手机网站支付接口2.0（外部商户创建订单并支付）
	 * @param map
	 * @return
	 */
	R alipayTradeWapPay(SysH5Goods sysH5Goods);

	
	/**
	 * alipay.trade.page.pay：统一收单下单并支付页面接口（PC场景下单并支付）
	 * @param map
	 * @return
	 */
	R alipayTradePagePay(Map<String, Object> map);
}
