package xin.yunhuo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 申请打款提交之后，返回的响应者
 */
@Data
@TableName("sys_yunhuo_response_msg")
public class YunhuoResponseMsg {
    @TableId(type = IdType.AUTO)
    private Integer id; // 数据库表的自增主键

    private String applyStatus; // 申请状态
    private String payChannel;  // 支付渠道
    private String paySerialNo; // 支付序列号
    private String platformId;  // 平台ID
    private String reqOrderNo;  // 请求订单号
    private String rspCod;      // 响应代码
    private String rspMsg;      // 响应信息
}