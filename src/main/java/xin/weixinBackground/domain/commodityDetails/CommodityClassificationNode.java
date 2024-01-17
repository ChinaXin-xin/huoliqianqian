package xin.weixinBackground.domain.commodityDetails;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分级表
 */
@Data
@TableName("wx_commodity_classification")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommodityClassificationNode {

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 商品名称
    private String commodityName;

    // 父节点是id
    private Integer parentId;

    @TableField(exist = false)
    private List<CommodityClassificationNode> children = new ArrayList<>();

    // 分类名下直属商品
    @TableField(exist = false)
    private List<CommodityDetails> commodityDetailsList;
}
