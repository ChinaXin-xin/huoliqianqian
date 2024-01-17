package xin.h5.domain.myselfCommercialTenant;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 我的商户中显示信息的查询载体
 */

@Data
public class DealAndPosMachineMsg {
    //商户昵称
    private String merchantName;

    //sn
    private String machineSn;

    //本月交易额
    private BigDecimal thisMonthGMV = BigDecimal.ZERO;

    //pos类型
    private String machineType;

    //pos机激活时间
    private Date updateTime;

    //pos机的商户号
    private String merchantId;
}
