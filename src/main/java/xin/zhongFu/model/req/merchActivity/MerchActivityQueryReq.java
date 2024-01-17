package xin.zhongFu.model.req.merchActivity;

import lombok.Data;
import xin.zhongFu.model.req.BaseReq;

@Data
public class MerchActivityQueryReq extends BaseReq {

	private String token;
	
	private String merchId;
	
	private String sn;
	
}
