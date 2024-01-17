package xin.zhongFu.model.req.agentExpandMerch;

import lombok.Data;
import xin.zhongFu.model.req.BaseReq;

@Data
public class AgentMerchTermReq extends BaseReq {

	private String token;

	private String merchId;
	
	private String termSn;
	
}
