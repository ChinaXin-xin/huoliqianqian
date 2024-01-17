package xin.weixinBackground.mapper.commodityDetails;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.weixinBackground.domain.commodityDetails.CommodityClassificationNode;

import java.util.List;

@Mapper
public interface CommodityClassificationMapper extends BaseMapper<CommodityClassificationNode> {

    @Select("select * from wx_commodity_classification where parent_id=#{parentId} ")
    List<CommodityClassificationNode> findByParentId(@Param("parentId") Integer parentId);
}
