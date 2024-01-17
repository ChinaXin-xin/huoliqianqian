package xin.admin.domain.earningsManagenment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 系统积分表实体类
 */
@Data
@TableName("user_score")
public class UserScore {

    /**
     * 主键自增长
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 外键，与sys_user的id绑定
     */
    private Long userId;

    /**
     * 这三个字段是从sys_user表中读取出来的,根据：userId
     */
    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private String phone;
    @TableField(exist = false)
    private String myselfNumber;

    /**
     * 奖励
     */
    private BigDecimal reward;

    /**
     * 机器SN
     */
    private String machineSn;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 详情
     */
    private String detail;

    /**
     * 时间
     */
    private Date time;
}
