package xin.zhongFu.model.resp;

import lombok.Data;

@Data
public class MerchActivityAmtResp{
	
	private String orderNo;

	private String traceNo;
	
	private String optNo;
	
	private String optTime;
	
	private String openDate;
	
	private String posCharge;
	
	private String vipCharge;
	
	private String simCharge;
	
	private String merchPayStatus;
	
	private String merchPayResult;
	
	private String merchPayTime;
	
	private String agentCollectStatus;
	
	private String agentCollectTime;
	
	private String agentOptNo;
	
	private String remark;
	
}
