package xin.admin.domain.membershipManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户提现管理
 */
@Data
@TableName("user_withdraw_management")
public class UserWithdrawManagement {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id; // 主键，自动增长

    // 启用了
    private Integer userAuthenticationInfoId; // 外键，绑定用户认证信息的uid，相当于sys_user的id

    @TableField(exist = false)
    private String memberName;  //提现人姓名
    @TableField(exist = false)
    private String memberPhone;  //提现人电话
    @TableField(exist = false)
    private String bankBranch; //提现人银行卡开户行
    @TableField(exist = false)
    private String bankCard;   //提现人银行卡

    private Float money; // 提现金额

    private String withdrawType; // 提现类型，与user_withdraw_type表的type字段绑定

    private Date withdrawTime; // 提现时间

    private Date approvalTime; // 审批时间

    private String remark;     // 说明

    private Integer status; // 状态（0：未审核，1：审核成功，2：审核拒绝）

    public Date getWithdrawTime() {
        if (withdrawTime == null)
            return new Date();
        return withdrawTime;
    }
}
