package xin.weixinBackground.mapper.commodityDetails;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import xin.weixinBackground.domain.commodityDetails.CommoditySpecification;

import java.util.List;

@Mapper
public interface CommoditySpecificationMapper extends BaseMapper<CommoditySpecification> {
    @Select("select * from wx_commodity_specification " +
            "where commodity_details_id=#{cs.commodityDetailsId} and color=#{cs.color} and style=#{cs.style} ")
    CommoditySpecification selectBySpecification(@Param("cs") CommoditySpecification cs);


    /**
     * 根据商品id，查询对应规格
     *
     * @param cdi
     * @return
     */
    @Select("select * from wx_commodity_specification where commodity_details_id = #{cdi}")
    List<CommoditySpecification> selectByCommodityDetailsId(@Param("cdi") Integer cdi);


    /**
     * 根据商品id，查询对应规格
     *
     * @param cdi
     * @return
     */
    @Select("select * from wx_commodity_specification where commodity_details_id = #{cdi} and id=#{id} ")
    List<CommoditySpecification> selectByCommodityDetailsIdAndSpecificationId(@Param("cdi") Integer cdi, @Param("id") Integer id);

    /**
     * 根据商品规格id，查询商品信息
     *
     * @param id
     * @return
     */
    @Select("select commodity_details_id from wx_commodity_specification where id= #{id} ")
    Integer selectByIdToDetailsId(@Param("id") Integer id);

    /**
     * 根据规格和数量去增加对应商品的销售数量
     *
     * @param id
     * @param num
     * @return
     */
    @Update("UPDATE wx_commodity_details\n" +
            "INNER JOIN wx_commodity_specification\n" +
            "ON wx_commodity_details.id = wx_commodity_specification.commodity_details_id\n" +
            "SET wx_commodity_details.market = wx_commodity_details.market + #{num} \n" +
            "WHERE wx_commodity_specification.id = #{id} ;\n")
    int updateMarket(@Param("id") Integer id, @Param("num") Integer num);

}
