package xin.weixin.controller.homePage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.weixin.service.homePage.HomePageService;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;

import java.util.List;

@RestController
@RequestMapping("/pro/homePage")
public class HomePageController {

    @Autowired
    HomePageService homePageService;

    /**
     * 首页的好物推荐，商品随机10条
     */
    @PostMapping("/getRandomRecommendedItems")
    ResponseResult<List<CommodityDetails>> getRandomRecommendedItems() {
        return homePageService.getRandomRecommendedItems();
    }
}
