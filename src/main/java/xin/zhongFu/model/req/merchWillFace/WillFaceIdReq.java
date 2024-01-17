package xin.zhongFu.model.req.merchWillFace;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.util.List;

@Data
public class WillFaceIdReq {

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
	
	//姓名
	//使用权威源比对时：姓名+证件号 必须输入
	//使用自带源比对时：姓名+证件号 可不输入
	private String name;
	
	//证件号码
	//使用权威源比对时：姓名+证件号 必须输入
	//使用自带源比对时：姓名+证件号 可不输入
	private String idNo;
	
	//用户 ID ，用户的唯一标识（不能带有特殊字符），需要跟生成签名的 userId 保持一致
	private String userId;
	
	//比对源照片，注意：原始图片不能超过500k，且必须为 JPG 或 PNG、BMP 格式
	//参数有值：使用合作伙伴提供的比对源照片进行比对，必须注意是正脸可信照片，照片质量由合作方保证
	//参数为空 ：根据身份证号 + 姓名使用权威数据源比对
	//BASE64String
	private String sourcePhotoStr;
	
	//比对源照片类型：
	//参数值为1 时是：水纹正脸照
	//参数值为 2 时是：高清正脸照
	//重要提示：照片上无水波纹的为高清照，请勿传错，否则影响比对准确率。如有疑问，请联系腾讯云技术支持线下确认
	//提供比对源照片需要传
	private String sourcePhotoType;
	
	//应用模式，意愿核身默认上传2
	private String liveService;
	
	//意愿核身类型：
	//产品服务类型为“2”时，需要填写参数，配置参数：0-问答模式，1-播报模式，默认配置为0
	private String willType;
	
	//意愿核身语言：
	//产品服务类型为“2”时，需要填写参数
	//配置：0-中文普通话，默认配置为0
	private String willLanguage;
	
	//意愿核身过程中播报文本/问题、用户朗读/回答的文本，当前支持一个播报文本+回答文本
	private List<WillFaceIdContentReq> willContentList;
	
	//播报问题文字语速：默认为1.0倍速
	//"-1"代表0.8倍；"0"代表1.0倍（默认）；"1"代表1.2倍；"1.5"代表1.35倍；"2"代表1.5倍
	private String speed;
	
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
