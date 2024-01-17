package xin.zhongFu.model.req.responseDomain;

import lombok.Data;

/**
 * API响应的顶层结构，返回商户基本信息。
 */
@Data
public class ResponseMerchantMsg {
    private String code; // 返回状态码
    private String message; // 返回信息
    private ResponseDataZF data; // 返回数据
}
