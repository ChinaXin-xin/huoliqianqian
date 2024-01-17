package xin.zhongFu.model.req;

import lombok.Data;

@Data
public class BaseReq {
	
	//服务商编号
	private String agentId;

	//签名
	private String sign;
	
}
