package xin.weixinBackground.service.commodityDetails;

import xin.admin.domain.ResponseResult;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;
import xin.common.domain.CommonalityQuery;

import java.util.List;

public interface CommodityDetailsService {
    ResponseResult add(CommodityDetails commodityDetails);

    ResponseResult update(CommodityDetails commodityDetails);

    ResponseResult<CommodityDetails> selectById(Integer id);

    ResponseResult<List<CommodityDetails>> selectByDetails(String details);

    ResponseResult delete(Integer id);

    ResponseResult<List<CommodityDetails>> selectByCommodityClassificationNodeId(Integer id);

    ResponseResult<CommonalityQuery<CommodityDetails>> select(CommonalityQuery<CommodityDetails> query);
}
