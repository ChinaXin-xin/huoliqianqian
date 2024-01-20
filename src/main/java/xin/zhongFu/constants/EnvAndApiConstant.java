package xin.zhongFu.constants;

/**
 * 环境与API接口信息
 * */
public class EnvAndApiConstant {
	
/*
	//环境信息地址
	public final static String ENV_ADDR_TEST = "http://39.103.165.38:38660";
	//环境密钥KEY
	public final static String ENV_TEST_KEY = "87200062532913E4";
	//环境服务商编号
	public final static String ENV_TEST_AGENT_ID = "87200062";
*/

	//环境信息地址
	public final static String ENV_ADDR_TEST = "http://121.89.222.153:38660";
	//环境密钥KEY
	public final static String ENV_TEST_KEY = "748003204755CC03";
	//环境服务商编号
	public final static String ENV_TEST_AGENT_ID = "74800320";
	
	//令牌API
	public final static String API_TOKEN = "/api/acq-channel-gateway/v1/acq-channel-auth-service/tokens/token";
	
	//商户服务费代收API
	public final static String API_MERCH_FEE_COLLECTION = "/api/acq-channel-gateway/v1/terminal-service/terms/activityReformV3/amountFrozen";
	
	//商户服务费代收查询API
	public final static String API_MERCH_FEE_COLLECTION_QUERY = "/api/acq-channel-gateway/v1/terminal-service/terms/activityReformV3/queryAmtInfo";
		
	//商户活动记录查询API
	public final static String API_MERCH_ACTIVITY_QUERY = "/api/acq-channel-gateway/v1/terminal-service/terms/activityReformV3/queryMerchWithAmtList";
	
	//短信模板查询API
	public final static String API_MERCH_SMS_QUERY = "/api/acq-channel-gateway/v1/terminal-service/terms/activityReformV3/querySmsList";
	
	//商户费率信息修改API
	public final static String API_MERCH_FEE_EDIT = "/api/acq-channel-gateway/v1/acq-channel-service/merchant/fee/updateNonAudit";
	
	//商户费率信息查询API
	public final static String API_MERCH_FEE_QUERY = "/api/acq-channel-gateway/v1/acq-channel-service/getMerchantFeeInfo";
	
	//商户双录结果查询API
	public final static String API_MERCH_WILL_FACE_QUERY = "/api/acq-channel-gateway/v1/acq-channel-service/merch/getWillFaceResult";

}
