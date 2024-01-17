package xin.zhongFu.model.resp;

import lombok.Data;

@Data
public class WillFaceResultValueResp {
	
	/**
	 *"result": {
	 *	"faceCode": "0",
		"faceMsg": "请求成功",
		"willCode": "0",
		"willMsg": "请求成功",
		"bizSeqNo": "23051120001184433418192720839761",
		"transactionTime": "20230511181927",
		"orderNo": "056f0d419726461d8232a5fd98663d40",
		"oriCode": "0",
		"appId": "IDAIQyjJ",
		"liveRate": "99",
		"similarity": "94.29",
		"occurredTime": "20230511180813",
		"photo": "base64编码字符串",
		"video": "base64编码字符串",
		"sdkVersion": "6.6.0",
		"willUserAudio":"base64编码字符串",
		"willReadAudio":"base64编码字符串",
		"willScrnShotImage":"base64编码字符串",
		"willUserVideo":"base64编码字符串",
		"willStandText": "您好，为确保您本人操作，此次签约全程录音录像。请问您本次业务是本人自愿办理吗？请回答：我确认",
		"willStandAnswer": "我确认",
		"willUserAnswer":"我确认",
		"riskInfo": {
			"deviceInfoLevel": "1",
			"deviceInfoTag": "",
			"riskInfoLevel": null,
			"riskInfoTag": null
		},
		"success": true
	},
	 */
	
	//人脸核身结果(0:成功,非0:失败)
	private String faceCode;
	
	//人脸核身结果描述
	private String faceMsg;
		
	//意愿表达结果(0:成功,非0:失败)
	private String willCode;
	
	//意愿表达结果描述
	private String willMsg;
	
	//业务流水号
	private String bizSeqNo;
	
	//请求接口的时间
	private String transactionTime;
	
	//订单编号
	private String orderNo;
	
	private String oriCode;
	
	//appid
	private String appId;
	
	//活体检测得分
	private String liveRate;
	
	//人脸比对得分
	private String similarity;
	
	//进行刷脸的时间
	private String occurredTime;
	
	//人脸核身时的照片，base64 位编码
	private String photo;
	
	//人脸核身时的视频，base64 位编码
	//注：该视频为活体阶段的1秒短视频
	private String video;
	
	//人脸核身时的 sdk 版本号
	private String sdkVersion;
	
	//意愿表达用户音频 意愿表达阶段的音频文件，base64位编码
	private String willUserAudio;
	
	//意愿表达播报音频 意愿表达阶段的音频文件，base64位编码
	private String willReadAudio;
	
	//意愿表达阶段的屏幕画面图，base64位编码
	private String willScrnShotImage;
	
	//意愿核身完整视频：从用户播报音频到回复音频过程，base64位编码
	//注意：如果功能配置为 SDK 返回视频，则本字段返回为空
	private String willUserVideo;
	
	//客户初始化上送的文字信息
	private String willStandText;
	
	//客户初始化上送的答案文字信息
	private String willStandAnswer;
	
	//识别结果文本信息
	private String willUserAnswer;
	
	//是否成功(boolean)
	private Boolean success;
	
}
