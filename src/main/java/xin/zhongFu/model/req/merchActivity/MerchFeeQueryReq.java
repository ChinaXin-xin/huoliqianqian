package xin.zhongFu.model.req.merchActivity;

import lombok.Data;
import xin.zhongFu.model.req.BaseReq;

@Data
public class MerchFeeQueryReq extends BaseReq {

	private String token;
	
	private String merchId;
	
}
