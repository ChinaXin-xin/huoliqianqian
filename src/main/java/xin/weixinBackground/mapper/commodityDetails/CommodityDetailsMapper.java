package xin.weixinBackground.mapper.commodityDetails;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;

import java.util.List;

@Mapper
public interface CommodityDetailsMapper extends BaseMapper<CommodityDetails> {
    // 随机获取n条商品
    List<CommodityDetails> selectRandomCommodities(int count);

    @Select("select * from wx_commodity_details where commodity_classification_node_id = #{CCId}")
    List<CommodityDetails> selectByCommodityClassificationId(@Param("CCId") int CCId);
}
