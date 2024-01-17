package xin.admin.domain.contentManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_msg")
public class SysMsg {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("weight")
    private Integer weight; // 权重

    @TableField("description")
    private String description; // 简介

    @TableField("status")
    private Integer status; // 状态

    @TableField("add_time")
    private Date addTime; // 添加时间

    @TableField("detail")
    private String detail; // 详情

    public Date getAddTime() {
        if (addTime == null)
            return new Date();
        return addTime;
    }
}
