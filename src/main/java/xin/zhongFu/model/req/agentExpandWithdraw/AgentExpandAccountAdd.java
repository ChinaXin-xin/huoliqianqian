package xin.zhongFu.model.req.agentExpandWithdraw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

/**
 * 代理商账户表查询参数类
 */
@Data
public class AgentExpandAccountAdd {

    String agentId;
    
    private String token;
    
    private String reqFlowNo;
    
    private String subAgentName;
    
    private String accountType;
    
    private String accountId;
    
    private String accountName;
    
    private String accountMobile;
    
    private String idCard;
    
    private String bankCode;
    
    private String bankName;
    
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
