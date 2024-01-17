package xin.zhongFu.model.req.agentExpandMerch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

/**
 * 代理商账户表查询参数类
 */
@Data
public class AgentMerchCombinedUpdate {

	private String agentId;
    
    private String merchId;
    
    //合并到账标识(0:笔笔到账,1:合并到账)
    private String mergeFlag;
    
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
