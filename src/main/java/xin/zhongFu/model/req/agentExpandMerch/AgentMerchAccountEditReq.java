package xin.zhongFu.model.req.agentExpandMerch;

import lombok.Data;
import xin.zhongFu.model.req.BaseReq;
import xin.zhongFu.model.req.ImageAO;

import java.util.List;

@Data
public class AgentMerchAccountEditReq extends BaseReq {

	private String token;

	private String merchId;
	
	private String accountId;
	
	private String accountMobile;
	
	private String bankCode;
	
	private String bankName;
	
	private List<ImageAO> imgList;
	
}
