package xin.weixin.controller.homePage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;
import xin.weixinBackground.service.commodityDetails.CommodityDetailsService;

/**
 * 商品列表功能
 * 商品详情
 */
@RestController
@RequestMapping("/pro/commodityDetail")
public class WxCommodityDetailsController {

    @Autowired
    CommodityDetailsService commodityDetailsService;

    /**
     * 查询指定id的商品信息
     *
     * @param id
     * @return
     */
    @PostMapping("/selectById/{id}")
    ResponseResult<CommodityDetails> selectById(@PathVariable Integer id) {
        return commodityDetailsService.selectById(id);
    }
}
