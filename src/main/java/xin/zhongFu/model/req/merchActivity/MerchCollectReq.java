package xin.zhongFu.model.req.merchActivity;

import lombok.Data;
import xin.zhongFu.model.req.BaseReq;

@Data
public class MerchCollectReq extends BaseReq {

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
