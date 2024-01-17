package xin.h5.domain.personalInformation;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 本月业绩中的五条信息
 * 每个pos机就是一个商户
 */
@Data
public class DealPerformance {
    private Date time;

    //显示的金额 -
    private BigDecimal money = BigDecimal.ZERO;

    //新增伙伴
    private Integer newPartner = 0;

    //累计伙伴
    private Integer accumulativeTotalPartner = 0;


    //新增激活pos，包括自己与下级的
    private Integer newActivatePos = 0;


    //累计激活pos，包括自己与下级的
    private Integer accumulativeTotalActivatePos = 0;
}
