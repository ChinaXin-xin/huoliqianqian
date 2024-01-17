package xin.weixinBackground.domain.commodityDetails;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;

import java.util.List;

/**
 * 商品详情表
 */
@Data
@TableName(value = "wx_commodity_details", autoResultMap = true)
public class CommodityDetails {

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 商品所属分类
    private Integer commodityClassificationNodeId;

    // 商品分类，所属分类的id，前端需要[1,3]
    @TableField(exist = false)
    private List<Integer> commodityClassificationNodeIdList;

    // 商品名称，也就是商品详情
    private String details;

    // 商品logo的图片的在线地址
    private String logoUrl;

    // 商品轮播图

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private String[] slidesShowUrlList;

    // 规格一的名称
    private String specificationOneName;

    // 规格一的细分型号
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private String[] specificationOneSubName;

    // 规格二的名称
    private String specificationTwoName;

    // 规格二的细分型号

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private String[] specificationTwoSubName;

    // 商品详情的图片

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private String[] detailsImgList;

    // 一共销售量多少件
    private Integer market;

    @TableField(exist = false)
    // 商品规格
    private List<CommoditySpecification> commoditySpecificationList;
}
