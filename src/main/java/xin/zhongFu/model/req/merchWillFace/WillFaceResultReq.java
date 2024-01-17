package xin.zhongFu.model.req.merchWillFace;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

@Data
public class WillFaceResultReq {

    //WBappid
	private String appId;
	
	//订单号，由合作方上传，字母/数字组成的字符串，每次唯一，不能超过32位
	private String orderNo;
	
	//默认参数值为：1.0.0
	private String version;
	
	//签名
	private String sign;
	
	//随机数
	private String nonce;
	
	//是否需要获取人脸识别的视频和文件，值为1则返回视频和照片、值为2则返回照片、值为3则返回视频；其他则不返回
	private String getFile;
	
	//是否需要获取意愿表达的音视频文件，默认值为1
	//值为1-返回所有音视频文件，值为2-不返回完整的视频文件，返回其他音视频文件
	private String getWillFile;
	
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
