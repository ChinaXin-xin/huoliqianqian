package xin.weixin.service.homePage;

import xin.admin.domain.ResponseResult;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;

import java.util.List;

public interface HomePageService {
    /**
     * 首页的好物推荐，商品随机10条
     */
    ResponseResult<List<CommodityDetails>> getRandomRecommendedItems();
}
