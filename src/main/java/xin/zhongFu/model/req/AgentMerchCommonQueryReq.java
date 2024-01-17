package xin.zhongFu.model.req;

import lombok.Data;

@Data
public class AgentMerchCommonQueryReq extends BaseReq{

	private String token;

	private String traceNo;
	
	private String merchId;
	
	private String directAgentId;
	
	private String sn;
	
	private String posCharge;
	
	private String vipCharge;
	
	private String simCharge;
	
	private String smsSend;
	
	private String smsCode;
	
}
