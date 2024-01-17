package xin.admin.domain.other;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体类：SysPosType
 * 对应数据库中的 sys_pos_type 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_pos_type")
public class SysPosType {
    public SysPosType(String type) {
        this.type = type;
    }

    /**
     * 主键，自增长
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机具类型
     */
    private String type;

    /**
     * 备注
     */
    private String remark;
}
