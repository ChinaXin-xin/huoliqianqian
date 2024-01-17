package xin.admin.domain.contentManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_banner")
public class SysBanner {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("weight")
    private Integer weight;

    @TableField("name")
    private String name;

    @TableField("image_addr")
    private String imageAddr;

    @TableField("status")
    private Integer status;

    @TableField("create_date")
    private Date createDate;

    public Date getCreateDate() {
        if (createDate == null)
            return new Date();
        return createDate;
    }
}
