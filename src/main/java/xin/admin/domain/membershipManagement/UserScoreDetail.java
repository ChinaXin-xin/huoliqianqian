package xin.admin.domain.membershipManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 积分明细类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_score_details")
public class UserScoreDetail implements Serializable {

    private static final Long serialVersionUID = -4398573894579345L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("member_phone")
    private String memberPhone;

    @TableField("change_points")
    private Integer changePoints;

    @TableField("points_before_change")
    private Integer pointsBeforeChange;

    @TableField("description")
    private String description;

    @TableField("source")
    private String source;

    @TableField("creation_time")
    private Date creationTime;

    public Date getCreationTime() {
        if (creationTime != null)
            return creationTime;
        else
            return new Date();
    }
}
