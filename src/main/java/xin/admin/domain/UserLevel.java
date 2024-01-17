package xin.admin.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_user_level")
public class UserLevel implements Serializable {
    private static final long serialVersionUID = -40356786663868312L;

    @TableId(type = IdType.AUTO)
    private Long id;

    //名称
    private String name;

    //升级量（万元）
    private Float upgradeVolume;

    //分成（万分之）
    private Float profitSplit;

    //添加时间
    private Date addTime;

    public Date getAddTime() {
        if (addTime==null)
            return new Date();
        else
            return addTime;
    }
}
