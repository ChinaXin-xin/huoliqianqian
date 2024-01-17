package xin.h5.domain.wallet;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * SysAward 实体类，对应数据库中的 sys_award 表。
 * 使用 MyBatis-Plus 和 Lombok 进行注解和简化代码。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_award")
public class SysAward {

    @TableId
    private Long uid; // 主键，uid绑定sys_user的主键，id

    private Boolean activationAward; // 激活奖
    private Boolean activityAward;   // 活动奖
    private Boolean achievementAward; // 达标奖
    private Boolean teamAward;        // 团队奖
    private Boolean cheerAward;       // 加油奖
    private Boolean referralReward;   // 推荐奖
    private Boolean profitSharingReward; // 分润奖

    @TableField(exist = false)
    private String bankCard;  //银行卡号

    @TableField(exist = false)
    private String phone;  //手机卡号，用户实名认证时的手机号

    @TableField(exist = false)
    private Float money; //可提现的金额
}
