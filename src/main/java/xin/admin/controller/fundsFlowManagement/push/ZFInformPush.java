package xin.admin.controller.fundsFlowManagement.push;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 中付的推送格式
 * @param <T> 推送的具体内容
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZFInformPush<T> {
    private String configAgentId;  // 必填 (M), 字符串, 交易通知配置机构号
    private String dataType;       // 必填 (M), 字符串, 推送数据类型，0：绑机通知；1：交易通知；
    private String sendBatchNo;    // 必填 (M), 字符串, 交易通知推送批次号
    private int sendNum;           // 必填 (M), 整数, 数据的推送条数
    private String sendTime;       // 必填 (M), 字符串, 数据的推送时间 yyyymmddhhmmss
    private String transDate;      // 必填 (M), 字符串, 交易日期 yyyymmdd（收单系统，交易发生的日期）
    private List<T> dataList;      // 必填 (M), 列表, 推送的数据列表(此参数不参与计算签名)。详见：0：绑机通知 1：交易通知
    private String sign;           // 必填 (M), 字符串, 签名, 签名计算方法 测试计算签名使用到的KEY ： 同步通知测试密钥
}
