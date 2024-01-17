package xin.zhongFu.model.resp;

import lombok.Data;

/**
 * 代理商下属机构账户修改-对外
 */
@Data
public class AgentExpandAccountResp {

    //机构编号
    private String agentId;
    
    //账户唯一标识
    private String accountUuid;
    
    //代理商下属机构名称
    private String subAgentName;

    //账户性质:S:对私，G:对公
    private String accountType;
    
    //账户账号
    private String accountId;
    
    //账户户名
    private String accountName;
    
    //身份证号码
    private String idCard;
    
    //预留手机号码
    private String accountMobile;
    
    //开户行行号
    private String bankCode;
    
    //开户行名称
    private String bankName;
    
    //请求流水号
    private String reqFlowNo;
    
    //同步状态(0:新增未报备,1:新增提交成功待审核,2:新增报备成功,3:新增报备失败,4:修改未报备,5:修改报备提交成功待审核,6:修改报备成功,7:修改报备失败)
    private String syncFlag;
    
    //同步描述
    private String syncDesc;
    
    //报备时间
    private String syncTime;
    
    //请求时间
    private String reqTime;
    
    //身份证图片base64字符串
    private String idCardFrontImg;
    
    //身份证反面base64字符串
    private String idCardBackImg;
    
    //银行图片base64字符串
    private String bankCardImg;
    
    @Override
    public String toString() {
    	return "AgentExpandAccountDTO [agentId=" + agentId + ", accountUuid=" + accountUuid + ", subAgentName=" + subAgentName
				+ ", accountType=" + accountType + ", bankCode=" + bankCode + ", bankName=" + bankName
				+ ", reqFlowNo=" + reqFlowNo + ", syncFlag=" + syncFlag + ", syncDesc=" + syncDesc
				+ ", syncTime=" + syncTime + ", reqTime=" + reqTime + ", accountName=" + accountName 
				+ "]";
    }
    
}
