package xin.h5.domain.personalInformation;

import lombok.Data;

import java.math.BigDecimal;

/**
 * h5首页的用户总和
 */
@Data
public class AccumulatedTotal {
    //累计交易额，分
    private BigDecimal accumulatedTotalMoney = BigDecimal.ZERO;

    //累计激活
    private Integer accumulatedTotalActivatePosNum = 0;

    //累计盟友总和
    private Integer accumulatedTotalOurPersonnelNum = 0;
}
