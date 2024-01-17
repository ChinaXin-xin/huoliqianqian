package xin.weixinBackground.controller.commodityDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;
import xin.common.domain.CommonalityQuery;
import xin.weixinBackground.service.commodityDetails.CommodityDetailsService;

import java.util.List;

/**
 * 商品列表功能
 * 商品详情
 */
@RestController
@RequestMapping("/admin/commodityDetail")
public class CommodityDetailsController {

    @Autowired
    CommodityDetailsService commodityDetailsService;

    @PostMapping("/add")
    ResponseResult add(@RequestBody CommodityDetails commodityDetails) {
        return commodityDetailsService.add(commodityDetails);
    }

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

    /**
     * 查询包含关键字的商品信息
     *
     * @param details
     * @return
     */
    @PostMapping("/selectByDetails")
    ResponseResult<List<CommodityDetails>> selectByDetails(@RequestBody String details) {
        return commodityDetailsService.selectByDetails(details);
    }

    /**
     * 根据分类的id，查询旗下的商品
     *
     * @param id
     * @return
     */
    @PostMapping("/selectByCommodityClassificationNodeId/{id}")
    ResponseResult<List<CommodityDetails>> selectByCommodityClassificationNodeId(@PathVariable Integer id) {
        return commodityDetailsService.selectByCommodityClassificationNodeId(id);
    }

    @PostMapping("/update")
    ResponseResult update(@RequestBody CommodityDetails commodityDetails) {
        return commodityDetailsService.update(commodityDetails);
    }

    @PostMapping("/delete/{id}")
    ResponseResult delete(@PathVariable Integer id) {
        return commodityDetailsService.delete(id);
    }

    @PostMapping("/select")
    ResponseResult<CommonalityQuery<CommodityDetails>> select(@RequestBody CommonalityQuery<CommodityDetails> query) {
        return commodityDetailsService.select(query);
    }
}
