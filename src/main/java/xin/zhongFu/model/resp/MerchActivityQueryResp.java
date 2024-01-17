package xin.zhongFu.model.resp;

import lombok.Data;

import java.util.List;

@Data
public class MerchActivityQueryResp{

	private String orderNo;
	
	private String merchId;
	
	private String agentId;
	
	private String sn;
	
	private String status;
	
	private String optnDate;
	
	private String termSort;
	
	private String remark;
	
	private List<MerchActivityAmtResp> amtList;
	
}
