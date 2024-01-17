package xin.admin.domain.earningsManagenment;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sys_referral_reward")
public class SysReferralReward {
    private Integer id;
    private Long userId;
    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private String phone;
    @TableField(exist = false)
    private String myselfNumber;
    private BigDecimal rewardAmount;
    private Date rewardTime;
}
