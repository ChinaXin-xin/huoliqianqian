package xin.zhongFu.model.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

/**
 * 代理商
 */
@Data
public class AgentCommonReq {

    /**
     * 机构编号
     */
    private String agentId;
    
    private String provinceCode;
    
    private String cityCode;
    
    //银行查询 关键词
    private String bankNameKeyword;
    
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
