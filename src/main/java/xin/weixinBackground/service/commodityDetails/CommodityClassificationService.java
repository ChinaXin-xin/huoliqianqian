package xin.weixinBackground.service.commodityDetails;

import xin.admin.domain.ResponseResult;
import xin.weixinBackground.domain.commodityDetails.CommodityClassificationNode;

import java.util.List;

public interface CommodityClassificationService {
    ResponseResult add(CommodityClassificationNode commodityClassification);

    /**
     * 返回所有商品分类的树形结构
     *
     * @return
     */
    ResponseResult<CommodityClassificationNode> select();



    ResponseResult<CommodityClassificationNode> selectById(Integer id);

    /**
     * 根据id，查询所有子分类与自己直属分类下的所有商品信息
     *
     * @return
     */
    ResponseResult<CommodityClassificationNode> selectByAllMsg(Integer id);

    ResponseResult delete(Integer id);

    ResponseResult update(CommodityClassificationNode commodityClassification);

    /**
     * 根据id，返回所有的上级，数据33的id，
     * 例如：11->22->33
     * 输入 33
     * 返回 22 33
     *
     * @param id
     * @return
     */
    List<CommodityClassificationNode> getAllParentClassifications(Integer id);

}
