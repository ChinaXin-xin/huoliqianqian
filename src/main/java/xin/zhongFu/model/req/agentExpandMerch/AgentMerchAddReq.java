package xin.zhongFu.model.req.agentExpandMerch;

import lombok.Data;
import xin.zhongFu.model.req.BaseReq;
import xin.zhongFu.model.req.ImageAO;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AgentMerchAddReq extends BaseReq {

	private String token;

	private String reqFlowNo;
	
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
	
	private BigDecimal cFeeRate;
	
	private BigDecimal	dFeeRate;
	
	private BigDecimal dFeeMax;
	
	private BigDecimal wechatPayFeeRate;
	
	private BigDecimal alipayFeeRate;
	
	private BigDecimal ycFreeFeeRate;
	
	private BigDecimal ydFreeFeeRate;
	
	private BigDecimal d0FeeRate;
	
	private BigDecimal d0SingleCashDrawal;
	
	private String accountType;
	
	private String accountId;
	
	private String accountName;
	
	private String accountMobile;
	
	private String accountIdCard;
	
	private String bankCode;
	
	private String bankName;
	
	private String termSn;
	
	private List<ImageAO> imgList;
	
}
