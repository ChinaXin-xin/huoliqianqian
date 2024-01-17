package xin.zhongFu.model.resp;

import lombok.Data;

@Data
public class MerchFeeQueryResp{

	private String merchId;
	
	private String agentId;
	
	private String cFeeRate;
	
	private String dFeeRate;
	
	private String dFeeMax;
	
	private String wechatPayFeeRate;
	
	private String alipayFeeRate;
	
	private String ycFreeFeeRate;
	
	private String ydFreeFeeRate;
	
	private String d0FeeRate;
	
	private String d0SingleCashDrawal;
	
	private String status;
	
}
