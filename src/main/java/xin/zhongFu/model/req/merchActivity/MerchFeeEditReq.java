package xin.zhongFu.model.req.merchActivity;

import lombok.Data;
import xin.zhongFu.model.req.BaseReq;

@Data
public class MerchFeeEditReq extends BaseReq {

	private String token;
	
	private String merchId;
	
	private String cFeeRate;
	
	private String dFeeRate;
	
	private String dFeeMax;
	
	private String wechatPayFeeRate;
	
	private String alipayFeeRate;
	
	private String ycFreeFeeRate;
	
	private String ydFreeFeeRate;
	
	private String d0FeeRate;
	
	private String d0SingleCashDrawal;
	
}
