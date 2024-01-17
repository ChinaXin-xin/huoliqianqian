package xin.admin.domain.membershipManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_fees")
public class UserFees {
    @TableId(type = IdType.AUTO)
    private int id;
    // 激活奖税费(%)
    private float activationTaxFee;
    // 推荐奖税费(%)
    private float referralTaxFee;
    // 分润奖税费(%)
    private float profitSharingTaxFee;
    // 活动奖税费(%)
    private float activityTaxFee;
    // 团队奖税费(%)
    private float teamTaxFee;
    // 提现手续费：元/笔
    private float withdrawalFee;
    // 提现扣货款(%)
    private float withdrawalDeductionRate;
    // 达标奖
    private float achievementAward;
    // 加油奖
    private float cheerAward;
}
