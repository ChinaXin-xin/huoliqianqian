package xin.h5.domain.myselfMachine;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MerchantDetails {
    private String icon; //头像在线地址，sys_user
    private String who; //商户姓名，真实姓名，sys_user
    private String clazz;        //机具类型 sys_pos_terminal
    private Date createTime;     //入网时间 sys_pos_terminal
    private Date updateTime;           //设置激活时间，sys_pos_terminal
    private String machineNo;          //sn码，sys_pos_terminal

    @TableField(exist = false)
    private BigDecimal thisMonthMoney; //本月交易额

    @TableField(exist = false)
    private BigDecimal historyMoney;   //历史交易额

    @TableField(exist = false)
    private Integer posNum;  //本月pos机刷了几笔
    @TableField(exist = false)
    private BigDecimal posMoney = BigDecimal.ZERO;  //本月pos刷了多少钱 -

    @TableField(exist = false)
    private Integer ysfNum;  //本月云闪付机刷了几笔
    @TableField(exist = false)
    private BigDecimal ysfMoney = BigDecimal.ZERO;  //本月云闪付刷了多少钱

    @TableField(exist = false)
    private Integer smNum;  //本月扫码机刷了几笔
    @TableField(exist = false)
    private BigDecimal smMoney = BigDecimal.ZERO;  //本月扫码刷了多少钱

    public BigDecimal getThisMonthMoney() {
        return posMoney.add(ysfMoney).add(smMoney);
    }
}
