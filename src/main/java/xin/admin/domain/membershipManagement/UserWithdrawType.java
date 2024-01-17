package xin.admin.domain.membershipManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提现管理模块中的类型
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_withdraw_type")
public class UserWithdrawType {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String type;
}
