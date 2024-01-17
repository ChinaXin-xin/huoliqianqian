package xin.h5.zfb.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/wechatpay.properties")
public class WechatPayProperties {

	/**
	 * 公众平台的appid（H5和app）
	 */
	@Value("${wechat_app_appid}")
	private String wechat_app_appid;
	/**
	 * 公众平台的appid（公众号）
	 */
	@Value("${wechat_jsapi_appid}")
	private String wechat_jsapi_appid;
	
	/**
	 * 商户秘钥
	 */
	@Value("${wechat_partner_key}")
	private String wechat_partner_key;
	
	/**
	 * 公众平台的商户号
	 */
	@Value("${wechat_mch_id}")
	private String wechat_mch_id;
	
	/**
	 * AppSecret（H5和app）
	 */
	@Value("${wechat_app_app_secrut}")
	private String wechat_app_app_secrut;
	/**
	 * AppSecret（公众号）
	 */
	@Value("${wechat_jsapi_app_secrut}")
	private String wechat_jsapi_app_secrut;
	
	/**
	 * 微信统一下单接口
	 */
	@Value("${wechat_create_order_url}")
	private String wechat_create_order_url;
	/**
	 * 微信查询订单接口
	 */
	@Value("${wechat_create_order_query_url}")
	private String wechat_create_order_query_url;
	/**
	 * 微信退款订单接口
	 */
	@Value("${wechat_create_order_refund_url}")
	private String wechat_create_order_refund_url;
	/**
	 * 微信退款订单查询接口
	 */
	@Value("${wechat_create_order_refund_query_url}")
	private String wechat_create_order_refund_query_url;
	
	/**
	 * WAP网站URL地址
	 */
	@Value("${wechat_wap_wap_url}")
	private String wechat_wap_wap_url;
	/**
	 * WAP网站名
	 */
	@Value("${wechat_wap_name}")
	private String wechat_wap_name;
	/**
	 * 签名类型
	 */
	@Value("${wechat_sign_type}")
	private String wechat_sign_type;
	
	
	/**
	 * 余额充值：服务器异步通知路径
	 */
	@Value("${wechat_mweb_recharge_notify_url}")
	private String wechat_mweb_recharge_notify_url;
	/**
	 * 余额充值：支付完成跳转路径
	 */
	@Value("${wechat_mweb_recharge_return_url}")
	private String wechat_mweb_recharge_return_url;
	
	/**
	 * 商城订单：服务器异步通知路径
	 */
	@Value("${wechat_mweb_order_notify_url}")
	private String wechat_mweb_order_notify_url;
	/**
	 * 商城订单：支付完成跳转路径
	 */
	@Value("${wechat_mweb_order_return_url}")
	private String wechat_mweb_order_return_url;
	/**
	 * 商城订单：支付完成跳转页面
	 */
	@Value("${wechat_mweb_order_refund_notify_url}")
	private String wechat_mweb_order_refund_notify_url;
	
	/**
	 * 支付类型：H5支付标记
	 */
	@Value("${wechat_trade_type_MWEB}")
	private String wechat_trade_type_MWEB;
	/**
	 * 支付类型：微信公众号支付
	 */
	@Value("${wechat_trade_type_JSAPI}")
	private String wechat_trade_type_JSAPI;
	/**
	 * 支付类型：微信APP支付
	 */
	@Value("${wechat_trade_type_APP}")
	private String wechat_trade_type_APP;
	public String getWechat_app_appid() {
		return wechat_app_appid;
	}
	public String getWechat_jsapi_appid() {
		return wechat_jsapi_appid;
	}
	public String getWechat_partner_key() {
		return wechat_partner_key;
	}
	public String getWechat_mch_id() {
		return wechat_mch_id;
	}
	public String getWechat_app_app_secrut() {
		return wechat_app_app_secrut;
	}
	public String getWechat_jsapi_app_secrut() {
		return wechat_jsapi_app_secrut;
	}
	public String getWechat_create_order_url() {
		return wechat_create_order_url;
	}
	public String getWechat_create_order_query_url() {
		return wechat_create_order_query_url;
	}
	public String getWechat_create_order_refund_url() {
		return wechat_create_order_refund_url;
	}
	public String getWechat_create_order_refund_query_url() {
		return wechat_create_order_refund_query_url;
	}
	public String getWechat_wap_wap_url() {
		return wechat_wap_wap_url;
	}
	public String getWechat_wap_name() {
		return wechat_wap_name;
	}
	public String getWechat_sign_type() {
		return wechat_sign_type;
	}
	public String getWechat_mweb_recharge_notify_url() {
		return wechat_mweb_recharge_notify_url;
	}
	public String getWechat_mweb_recharge_return_url() {
		return wechat_mweb_recharge_return_url;
	}
	public String getWechat_mweb_order_notify_url() {
		return wechat_mweb_order_notify_url;
	}
	public String getWechat_mweb_order_return_url() {
		return wechat_mweb_order_return_url;
	}
	public String getWechat_mweb_order_refund_notify_url() {
		return wechat_mweb_order_refund_notify_url;
	}
	public String getWechat_trade_type_MWEB() {
		return wechat_trade_type_MWEB;
	}
	public String getWechat_trade_type_JSAPI() {
		return wechat_trade_type_JSAPI;
	}
	public String getWechat_trade_type_APP() {
		return wechat_trade_type_APP;
	}
	public void setWechat_app_appid(String wechat_app_appid) {
		this.wechat_app_appid = wechat_app_appid;
	}
	public void setWechat_jsapi_appid(String wechat_jsapi_appid) {
		this.wechat_jsapi_appid = wechat_jsapi_appid;
	}
	public void setWechat_partner_key(String wechat_partner_key) {
		this.wechat_partner_key = wechat_partner_key;
	}
	public void setWechat_mch_id(String wechat_mch_id) {
		this.wechat_mch_id = wechat_mch_id;
	}
	public void setWechat_app_app_secrut(String wechat_app_app_secrut) {
		this.wechat_app_app_secrut = wechat_app_app_secrut;
	}
	public void setWechat_jsapi_app_secrut(String wechat_jsapi_app_secrut) {
		this.wechat_jsapi_app_secrut = wechat_jsapi_app_secrut;
	}
	public void setWechat_create_order_url(String wechat_create_order_url) {
		this.wechat_create_order_url = wechat_create_order_url;
	}
	public void setWechat_create_order_query_url(String wechat_create_order_query_url) {
		this.wechat_create_order_query_url = wechat_create_order_query_url;
	}
	public void setWechat_create_order_refund_url(String wechat_create_order_refund_url) {
		this.wechat_create_order_refund_url = wechat_create_order_refund_url;
	}
	public void setWechat_create_order_refund_query_url(String wechat_create_order_refund_query_url) {
		this.wechat_create_order_refund_query_url = wechat_create_order_refund_query_url;
	}
	public void setWechat_wap_wap_url(String wechat_wap_wap_url) {
		this.wechat_wap_wap_url = wechat_wap_wap_url;
	}
	public void setWechat_wap_name(String wechat_wap_name) {
		this.wechat_wap_name = wechat_wap_name;
	}
	public void setWechat_sign_type(String wechat_sign_type) {
		this.wechat_sign_type = wechat_sign_type;
	}
	public void setWechat_mweb_recharge_notify_url(String wechat_mweb_recharge_notify_url) {
		this.wechat_mweb_recharge_notify_url = wechat_mweb_recharge_notify_url;
	}
	public void setWechat_mweb_recharge_return_url(String wechat_mweb_recharge_return_url) {
		this.wechat_mweb_recharge_return_url = wechat_mweb_recharge_return_url;
	}
	public void setWechat_mweb_order_notify_url(String wechat_mweb_order_notify_url) {
		this.wechat_mweb_order_notify_url = wechat_mweb_order_notify_url;
	}
	public void setWechat_mweb_order_return_url(String wechat_mweb_order_return_url) {
		this.wechat_mweb_order_return_url = wechat_mweb_order_return_url;
	}
	public void setWechat_mweb_order_refund_notify_url(String wechat_mweb_order_refund_notify_url) {
		this.wechat_mweb_order_refund_notify_url = wechat_mweb_order_refund_notify_url;
	}
	public void setWechat_trade_type_MWEB(String wechat_trade_type_MWEB) {
		this.wechat_trade_type_MWEB = wechat_trade_type_MWEB;
	}
	public void setWechat_trade_type_JSAPI(String wechat_trade_type_JSAPI) {
		this.wechat_trade_type_JSAPI = wechat_trade_type_JSAPI;
	}
	public void setWechat_trade_type_APP(String wechat_trade_type_APP) {
		this.wechat_trade_type_APP = wechat_trade_type_APP;
	}
	
}
