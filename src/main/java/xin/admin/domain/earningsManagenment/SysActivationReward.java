package xin.admin.domain.earningsManagenment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sys_activation_reward")
public class SysActivationReward {
    @TableId(type = IdType.AUTO)
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
