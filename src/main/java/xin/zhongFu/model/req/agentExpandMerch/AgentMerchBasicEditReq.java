package xin.zhongFu.model.req.agentExpandMerch;

import lombok.Data;
import xin.zhongFu.model.req.BaseReq;
import xin.zhongFu.model.req.ImageAO;

import java.util.List;

@Data
public class AgentMerchBasicEditReq extends BaseReq {

	private String token;

	private String merchId;
	
	private String contactsName;
	
	private String contactsMobile;
	
	private String idCard;
	
	private String idCardDateStart;
	
	private String idCardDateEnd;
	
	private String provinceName;
	
	private String provinceCode;
	
	private String cityName;
	
	private String cityCode;
	
	private String countyName;
	
	private String countyCode;
	
	private String busiAddr;
	
	private List<ImageAO> imgList;
	
}
