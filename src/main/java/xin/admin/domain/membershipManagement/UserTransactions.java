package xin.admin.domain.membershipManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 金额明细
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_transactions")
public class UserTransactions {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String memberPhone;
    private Float amountChanged;
    private Float amountBeforeChange;
    private String description;
    private Date createdAt;
    private String type;
    private String source;
}
