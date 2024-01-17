package xin.weixin.level.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wx_user_gradation")
public class WxUserGradation {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userName;

    private Integer parentId;
}
