package xin.zhongFu.model.req.agentExpandMerch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 商户信息查询
 */
@Data
public class AgentExpandMerchWithdrawApplyAO {
	
	@NotEmpty(message="机构编号为空")
	private String agentId;

	@NotEmpty(message="商户号为空")
	private String merchId;
	
	@NotEmpty(message="请求流水号为空")
	private String outReqFlowNo;

	//提现交易标识ID(多笔以逗号分割,例23,43,123)
	@NotEmpty(message="提现标识为空")
    private String withdrawId;
    
    //提现汇总金额
	@NotEmpty(message="提现金额为空")
    private String withdrawAmt;
	
	private String token;
    
    private String sign;
    
    @Override
    public String toString() {
        return JSON.toJSONString(this,
                                 new SerializerFeature[] {
            SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.UseISO8601DateFormat
        });
    }
    
}
