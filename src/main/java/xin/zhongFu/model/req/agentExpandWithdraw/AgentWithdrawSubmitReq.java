package xin.zhongFu.model.req.agentExpandWithdraw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 代理商提现发起
 */
@Data
public class AgentWithdrawSubmitReq {

    //代理商名称
    private String agentId;
    
    //账户唯一标识
    private String accountUuid;
	 
	//提现类型(1001:服务费,1002:流量费,1003:分润)
	private String withdrawType;
    
    //金额
    private BigDecimal amount;
    
    //请求流水号
    private String reqFlowNo;
    
    //备注
    private String remark;
    
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
