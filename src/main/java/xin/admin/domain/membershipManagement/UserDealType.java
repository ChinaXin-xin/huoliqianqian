package xin.admin.domain.membershipManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_deal_type")
public class UserDealType {
    @TableId(type = IdType.AUTO)
    private int id;
    @TableField("type")
    private String type;
}
