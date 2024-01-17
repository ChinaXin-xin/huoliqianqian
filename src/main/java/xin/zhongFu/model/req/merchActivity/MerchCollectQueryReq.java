package xin.zhongFu.model.req.merchActivity;

import lombok.Data;
import xin.zhongFu.model.req.BaseReq;

@Data
public class MerchCollectQueryReq extends BaseReq {

	private String token;
	
	private String traceNo;
	
	private String optNo;
	
}
