package xin.zhongFu.model.req;

import lombok.Data;

@Data
public class AgentOcrIdCardReq extends BaseReq{

	private String token;

	private String reqFlowNo;
	
	private String imgBase64Str;
	
	//身份证正反面(front:正面-人像面；back:反面-国徽面)
	private String idCardSide;
	
}
