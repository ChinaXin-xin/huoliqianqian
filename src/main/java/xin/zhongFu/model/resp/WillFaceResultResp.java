package xin.zhongFu.model.resp;

import lombok.Data;

@Data
public class WillFaceResultResp {
	
	//响应码(0:成功,非0:失败)
	private String code;
	
	//响应信息
	private String msg;
	
	//业务流水号
	private String bizSeqNo;
	
	//调用接口的时间
	private String transactionTime;
	
	//access_token 的值
	private WillFaceResultValueResp result;
	
}
