package xin.zhongFu.model.req.merchWillFace;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

@Data
public class WillFaceIdContentReq {

	//从下标0开始
	private String id;
	
	//系统播报问题文本/问题
	//长度为120个字以内
	//示例：“ 您好，为确保您本人操作，此次签约全程录音录像。请问您本次业务是本人自愿办理吗？请回答：我确认 ”
	private String question;
	
	//用户朗读/回答的文本，用于识别用户朗读/回答的语音与文本是否一致。长度为10个字以内
	//支持多个文本作为识别内容，文本之间用“|”符号分割。
	//示例：“我确认”
	private String answer;

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
