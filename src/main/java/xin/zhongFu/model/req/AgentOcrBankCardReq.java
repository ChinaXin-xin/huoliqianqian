package xin.zhongFu.model.req;

import lombok.Data;

@Data
public class AgentOcrBankCardReq extends BaseReq{

	private String token;

	private String reqFlowNo;
	
	private String imgBase64Str;
	
}
