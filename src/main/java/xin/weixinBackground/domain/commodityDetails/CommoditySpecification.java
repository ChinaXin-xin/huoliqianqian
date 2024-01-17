package xin.weixinBackground.domain.commodityDetails;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 商品的规格
 */
@Data
@TableName("wx_commodity_specification")
public class CommoditySpecification {

    @TableId(type = IdType.AUTO)
    private Integer id;

    //商品详情id，绑定wx_commodity_details表的id
    private Integer commodityDetailsId;
    // 规格-1
    private String color;
    // 规格-2
    private String style;
    // 库存
    private Integer stock;
    // 原价
    private Float sourcePrice;
    // 单价，计算用这个
    private Float singlePrice;
    // 预览图
    private String preview;
    // 规格编码
    private String code;
}
