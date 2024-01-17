package xin.h5.zfb.utils.pay.alipay;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.alipay.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.alipay.api.domain.AlipayFundTransOrderQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeOrderSettleModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.domain.OpenApiRoyaltyDetailInfoPojo;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import xin.h5.zfb.config.properties.AliPayProperties;


/**
 * 支付宝工具类
 * @author Administrator
 *
 */
@Component
public class AliPayUtil {
	
	@Autowired
	private AliPayProperties aliPayProperties ;

	private static AliPayProperties staticAliPayProperties;
	
	static AlipayClient alipayClient;
	
	/**
	 * 初始化
	 */
	@PostConstruct 
	public void init() {
		staticAliPayProperties = aliPayProperties;
		alipayClient = new DefaultAlipayClient(staticAliPayProperties.getAli_gatewayUrl(), staticAliPayProperties.getAli_app_id(), staticAliPayProperties.getAli_merchant_private_key(), "json", staticAliPayProperties.getAli_charset(), staticAliPayProperties.getAli_alipay_public_key(), staticAliPayProperties.getAli_sign_type());
	} 
	

	/**
	 * alipay.trade.wap.pay：H5手机网站支付接口2.0（外部商户创建订单并支付）
	 * @param out_trade_no：商户订单号
	 * @param total_amount：支付金额，单位：元
	 * @return
	 */
	public static String alipayTradeWapPay(String out_trade_no, String total_amount){
        try {
        	//（1）封装bizmodel信息
        	AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        	//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
    		model.setOutTradeNo(out_trade_no);
    		model.setSubject("支付宝手机网站支付");
    		model.setBody("支付宝手机网站支付");
    		model.setProductCode("QUICK_WAP_WAY");
    		model.setTotalAmount(total_amount);
    		model.setTimeoutExpress(staticAliPayProperties.getAli_timeout_express());
    		model.setQuitUrl(staticAliPayProperties.getAli_quit_url());
    		//（2）设置请求参数
			AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
			alipayRequest.setReturnUrl(staticAliPayProperties.getAli_return_url());
			alipayRequest.setNotifyUrl(staticAliPayProperties.getAli_notify_url());
			alipayRequest.setBizModel(model);                                                                                                                                                               
			//（3）请求
			AlipayTradeWapPayResponse alipayTradeWapPayResponse = alipayClient.pageExecute(alipayRequest);

			String form = alipayTradeWapPayResponse.getBody();
			System.out.println("*********************\n返回结果为："+form);
			return form;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	
	/**
	 * alipay.trade.page.pay：统一收单下单并支付页面接口（PC场景下单并支付）
	 * @param aliPayMap
	 * @return
	 */
	public static String alipayTradePagePay(String out_trade_no, String total_amount){
        try {
        	//（1）封装bizmodel信息
        	AlipayTradePagePayModel model = new AlipayTradePagePayModel();
    		model.setOutTradeNo(out_trade_no);
    		model.setSubject("支付宝电脑网站支付");
    		model.setBody("支付宝电脑网站支付");
    		model.setProductCode("FAST_INSTANT_TRADE_PAY");
    		model.setTotalAmount(total_amount);
    		model.setTimeoutExpress(staticAliPayProperties.getAli_timeout_express());
    		//（2）设置请求参数
    		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
			alipayRequest.setReturnUrl(staticAliPayProperties.getAli_return_url());
			alipayRequest.setNotifyUrl(staticAliPayProperties.getAli_notify_url());
			alipayRequest.setBizModel(model);                                                                                                                                                               
			//（3）请求
			String form = alipayClient.pageExecute(alipayRequest).getBody();
			System.out.println("*********************\n返回结果为："+form);
			return form;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	
	/**
	 * alipay.trade.app.pay：原生手机APP支付测试（外部商户APP唤起快捷SDK创建订单并支付）
	 * @param alipayInfo
	 * @return
	 */
	public static String alipayTradeAppPay(String out_trade_no, String total_amount){
        try {
        	//（1）封装bizmodel信息
        	AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
    		model.setOutTradeNo(out_trade_no);
    		model.setSubject("支付宝APP支付");
    		model.setBody("支付宝APP支付");
    		model.setProductCode("QUICK_MSECURITY_PAY");
    		model.setTotalAmount(total_amount);
    		model.setTimeoutExpress(staticAliPayProperties.getAli_timeout_express());
			//（2）设置请求参数
    		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
			AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
			alipayRequest.setNotifyUrl(staticAliPayProperties.getAli_notify_url());
			alipayRequest.setBizModel(model);                                                                                                                                                               
			//（3）请求
			//这里和普通的接口调用不同，使用的是sdkExecute
			String form = alipayClient.sdkExecute(alipayRequest).getBody();
			System.out.println("*********************\n返回结果为："+form);
			return form;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	
	/**
	 * alipay.trade.precreate：统一收单线下交易预创建（扫码支付、收银员通过收银台或商户后台调用支付宝接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付。）
	 * product_code参数可选：销售产品码===>FACE_TO_FACE_PAYMENT
	 * @param out_trade_no
	 * @param total_amount
	 * @return
	 */
	public static String alipayTradePrecreate(String out_trade_no, String total_amount){
		try {
			//（1）封装bizmodel信息
			AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
			model.setOutTradeNo(out_trade_no);
			model.setSubject("扫码支付：线下交易预创建");
			model.setBody("扫码支付：线下交易预创建");
			model.setTotalAmount(total_amount);
			model.setTimeoutExpress(staticAliPayProperties.getAli_timeout_express());
			//（2）设置请求参数
			AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
			alipayRequest.setNotifyUrl(staticAliPayProperties.getAli_notify_url());
			alipayRequest.setBizModel(model);  
			//（3）请求
			String form = alipayClient.execute(alipayRequest).getBody();
			System.out.println("*********************\n返回结果为："+form);
			return form;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * alipay.trade.pay：统一收单交易支付接口（条码支付、声波支付、收银员使用扫码设备读取用户手机支付宝“付款码”/声波获取设备（如麦克风）读取用户手机支付宝的声波信息后，将二维码或条码信息/声波信息通过本接口上送至支付宝发起支付。）
	 * product_code参数可选：销售产品码===>FACE_TO_FACE_PAYMENT
	 * @param out_trade_no
	 * @param total_amount
	 * @return
	 */
	public static String alipayTradePay(String auth_code, String out_trade_no, String total_amount){
		try {
			//（1）封装bizmodel信息
			AlipayTradePayModel model = new AlipayTradePayModel();
			model.setAuthCode(auth_code);
			model.setOutTradeNo(out_trade_no);
			model.setSubject("条码支付：线下交易预创建");
			model.setBody("条码支付：线下交易预创建");
			model.setTotalAmount(total_amount);
			model.setScene("bar_code");
			model.setTimeoutExpress(staticAliPayProperties.getAli_timeout_express());
			//（2）设置请求参数
			AlipayTradePayRequest alipayRequest = new AlipayTradePayRequest();
			alipayRequest.setNotifyUrl(staticAliPayProperties.getAli_notify_url());
			alipayRequest.setBizModel(model);  
			//（3）请求
			String form = alipayClient.execute(alipayRequest).getBody();
			System.out.println("*********************\n返回结果为："+form);
			return form;
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
     * alipay.fund.trans.toaccount.transfer：单笔转账到支付宝账户接口
     * @param out_trade_no
     * @param refund_amount
     * @return
     */
    public static boolean alipayFundTransToaccountTransfer(String out_biz_no, String total_amount, String payee_type, String payee_account, String payer_show_name, String payer_real_name) {
    	try {
			//（1）封装bizmodel信息
    		AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
			model.setOutBizNo(out_biz_no);//生成订单号
			model.setPayeeType(payee_type);//固定值
			model.setPayeeAccount(payee_account);//转账收款账户
			model.setAmount(total_amount);//转账金额
			model.setPayerShowName(payer_show_name);
			model.setPayerRealName(payer_real_name);//账户真实名称
			model.setRemark("javen测试单笔转账到支付宝");
			//（2）设置请求参数
			AlipayFundTransToaccountTransferRequest alipayRequest = new AlipayFundTransToaccountTransferRequest();
			alipayRequest.setBizModel(model);    
			//（3）请求
			AlipayFundTransToaccountTransferResponse alipayFundTransToaccountTransferResponse  = alipayClient.execute(alipayRequest);
			System.out.println("*********************\n返回结果为："+alipayFundTransToaccountTransferResponse);
			if(alipayFundTransToaccountTransferResponse.isSuccess()) {
				return true;
			}else {
				return alipayFundTransOrderQuery(out_biz_no, null);
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return false;
		}
    }
    
    
    /**
     * alipay.fund.trans.order.query：支付宝单笔转账订单查询
     * @param out_biz_no
     * @param total_amount
     * @param payee_account
     * @param payer_real_name
     * @return
     */
    public static boolean alipayFundTransOrderQuery(String out_biz_no, String order_id) {
    	try {
			//（1）封装bizmodel信息
    		AlipayFundTransOrderQueryModel model = new AlipayFundTransOrderQueryModel();
			model.setOutBizNo(out_biz_no);//生成订单号
			model.setOrderId(order_id);//支付宝转账单据号
			//（2）设置请求参数
			AlipayFundTransOrderQueryRequest alipayRequest = new AlipayFundTransOrderQueryRequest();
			alipayRequest.setBizModel(model);    
			//（3）请求
			AlipayFundTransOrderQueryResponse alipayFundTransOrderQueryResponse  = alipayClient.execute(alipayRequest);
			System.out.println("*********************\n返回结果为："+alipayFundTransOrderQueryResponse);
			if(alipayFundTransOrderQueryResponse.isSuccess()) {
				return true;
			}else {
				return false;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return false;
		}
    }
	
	
	/**
	 * alipay.trade.cancel
     * 支付宝撤销订单接口
     * @param out_trade_no
     * @param refund_amount
     * @return
     */
    public static boolean aliPayCancel(String out_trade_no, String trade_no) {
    	try {
			//（1）封装bizmodel信息
			AlipayTradeCancelModel model = new AlipayTradeCancelModel();
			model.setOutTradeNo(out_trade_no);//商户订单号
			model.setTradeNo(trade_no);//支付宝交易号
			//（2）设置请求参数
			AlipayTradeCancelRequest alipayRequest = new AlipayTradeCancelRequest();
			alipayRequest.setBizModel(model);    
			//（3）请求
			AlipayTradeCancelResponse alipayTradeCancelResponse  = alipayClient.execute(alipayRequest);
			System.out.println("*********************\n返回结果为："+alipayTradeCancelResponse);
			if(alipayTradeCancelResponse.isSuccess()) {
				return true;
			}else {
				return false;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return false;
		}
    }
    
    
    /**
	 * alipay.trade.create
     * 统一收单交易创建接口
     * @param out_trade_no
     * @param refund_amount
     * @return
     */
    public static String aliPayCreate(String out_trade_no, String total_amount) {
    	try {
			//（1）封装bizmodel信息
    		AlipayTradeCreateModel model = new AlipayTradeCreateModel();
			model.setOutTradeNo(out_trade_no);//商户订单号
			model.setTotalAmount(total_amount);//订单总金额，单位为元
			model.setSubject("统一收单交易创建接口");
			model.setBody("统一收单交易创建接口");
			//（2）设置请求参数
			AlipayTradeCreateRequest alipayRequest = new AlipayTradeCreateRequest();
			alipayRequest.setBizModel(model);    
			//（3）请求
			AlipayTradeCreateResponse alipayTradeCreateResponse  = alipayClient.execute(alipayRequest);
			return alipayTradeCreateResponse.getBody();	
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
    }

    
    /**
     * alipay.trade.close
     * 支付宝关闭订单接口
     * @param out_trade_no
     * @param refund_amount
     * @return
     */
    public static String aliPayClose(String out_trade_no, String trade_no) {
    	try {
			//（1）封装bizmodel信息
			AlipayTradeCloseModel model = new AlipayTradeCloseModel();
			model.setOutTradeNo(out_trade_no);//商户订单号
			model.setTradeNo(trade_no);//支付宝交易号
			//（2）设置请求参数
			AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
			alipayRequest.setBizModel(model);    
			//（3）请求
			AlipayTradeCloseResponse alipayTradeCloseResponse  = alipayClient.execute(alipayRequest);
			return alipayTradeCloseResponse.getBody();	
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    
    
    /**
     * alipay.trade.order.settle
     * 统一收单交易结算接口
     * @param out_trade_no
     * @param refund_amount
     * @return
     */
    public static boolean aliPaySettle(String out_request_no, String trade_no) {
    	try {
			//（1）封装bizmodel信息
    		AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
			model.setOutRequestNo(out_request_no);//结算请求流水号 开发者自行生成并保证唯一性
			model.setTradeNo(trade_no);//支付宝交易号
			List<OpenApiRoyaltyDetailInfoPojo> royaltyParameters = new ArrayList<OpenApiRoyaltyDetailInfoPojo>();
			model.setRoyaltyParameters(royaltyParameters);
			//（2）设置请求参数
			AlipayTradeOrderSettleRequest alipayRequest = new AlipayTradeOrderSettleRequest();
			alipayRequest.setBizModel(model);    
			//（3）请求
			AlipayTradeOrderSettleResponse alipayTradeOrderSettleResponse  = alipayClient.execute(alipayRequest);
			if(alipayTradeOrderSettleResponse.isSuccess()) {
				return true;
			}else {
				return false;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return false;
		}
    }
	
	
	 /**
	 * alipay.trade.refund
     * 支付宝退款接口
     * @param out_trade_no
     * @param refund_amount
     * @return
     */
    public static String aliPayRefund(String out_trade_no, String trade_no, String refund_amount, String out_request_no) {
    	try {
			//（1）封装bizmodel信息
			AlipayTradeRefundModel model = new AlipayTradeRefundModel();
			model.setOutTradeNo(out_trade_no);//商户订单号
			model.setTradeNo(trade_no);//支付宝交易号
			model.setRefundAmount(refund_amount);//退款金额
			model.setOutRequestNo(out_request_no);//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
			//（2）设置请求参数
			AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
			//alipayRequest.setNotifyUrl(staticAliPayProperties.getChain_wap_order_refund_notify_url());
			alipayRequest.setBizModel(model);    
			//（3）请求
			AlipayTradeRefundResponse  alipayTradeRefundResponse  = alipayClient.execute(alipayRequest);
			return alipayTradeRefundResponse.getBody();	
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    
    /**
     * alipay.trade.fastpay.refund.query
     * 支付宝退款查询接口
     * @param out_trade_no
     * @param refund_amount
     * @return
     */
    public static String aliPayRefundQuery(String out_trade_no, String trade_no, String out_request_no) {
    	try {
			//（1）封装bizmodel信息
    		AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
			model.setOutTradeNo(out_trade_no);//商户订单号
			model.setTradeNo(trade_no);//支付宝交易号
			model.setOutRequestNo(out_request_no);//标识一次退款请求
			//（2）设置请求参数
			AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();
			alipayRequest.setBizModel(model);    
			//（3）请求
			AlipayTradeFastpayRefundQueryResponse  alipayTradeFastpayRefundQueryResponse  = alipayClient.execute(alipayRequest);
			return alipayTradeFastpayRefundQueryResponse.getBody();	
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    
    /**
     * alipay.trade.query
     * 查询支付宝订单
     * @param outTradeNo
     * @param tradeNo
     * @return
     */
    public static String aliPayQuery(String out_trade_no, String trade_no) {
    	try {
    		//（1）封装bizmodel信息
    		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
    		model.setOutTradeNo(out_trade_no);//商户订单号
    		model.setTradeNo(trade_no);//支付宝交易号
    		//（2）设置请求参数
        	AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest(); 
        	queryRequest.setBizModel(model);
        	//（3）请求
        	AlipayTradeQueryResponse  alipayTradeQueryResponse  = alipayClient.execute(queryRequest);
        	return alipayTradeQueryResponse.getBody();
    	}catch(Exception e) {
    		e.printStackTrace();
			return null;
    	}
    }
    
    
    /**
     * alipay.data.dataservice.bill.downloadurl.query
     * 查询对账单下载地址
     * @param out_trade_no
     * @param trade_no
     * @return
     */
    public static String aliPayBillDownloadurlQuery(String bill_type, String bill_date) {
    	try {
    		//（1）封装bizmodel信息
    		AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
    		model.setBillType(bill_type);//账单类型
    		model.setBillDate(bill_date);//账单时间
    		//（2）设置请求参数
    		AlipayDataDataserviceBillDownloadurlQueryRequest queryRequest = new AlipayDataDataserviceBillDownloadurlQueryRequest(); 
        	queryRequest.setBizModel(model);
        	//（3）请求
        	AlipayDataDataserviceBillDownloadurlQueryResponse alipayDataDataserviceBillDownloadurlQueryResponse = alipayClient.execute(queryRequest);
        	return alipayDataDataserviceBillDownloadurlQueryResponse.getBillDownloadUrl();
    	}catch(Exception e) {
    		e.printStackTrace();
			return null;
    	}
    }

}
