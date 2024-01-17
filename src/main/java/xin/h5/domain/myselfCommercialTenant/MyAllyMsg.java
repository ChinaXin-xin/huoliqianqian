package xin.h5.domain.myselfCommercialTenant;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyAllyMsg {
    //名字
    private String name;

    //本月新增商户
    private Integer thisMonthNewMerchantNum;

    //自己名下历史激活数量
    private Integer historyMerchantNum;

    //本月交易额
    private BigDecimal thisMonthMoney;


    //历史交易额
    private BigDecimal historyMoney;

    //商户账号
    private String userName;
}
