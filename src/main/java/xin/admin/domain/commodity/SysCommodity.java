package xin.admin.domain.commodity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品新消息表
 */
@Data
@NoArgsConstructor
@TableName("sys_commodity")
public class SysCommodity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String category;
    private String name;
    private String imageUrl;
    private String carouselImages;
    private Float price;
    private Integer specification;
}
