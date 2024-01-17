package xin.weixinBackground.controller.commodityDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.weixinBackground.domain.commodityDetails.CommodityClassificationNode;
import xin.weixinBackground.service.commodityDetails.CommodityClassificationService;

/**
 * 商品分类
 */
@RestController
@RequestMapping("/admin/commodityClassification")
public class CommodityClassificationController {

    @Autowired
    CommodityClassificationService commodityClassificationService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody CommodityClassificationNode commodityClassification) {
        return commodityClassificationService.add(commodityClassification);
    }

    @PostMapping("/select")
    public ResponseResult select() {
        return commodityClassificationService.select();
    }

    @PostMapping("/selectById/{id}")
    public ResponseResult selectById(@PathVariable Integer id) {
        return commodityClassificationService.selectById(id);
    }

    @PostMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        return commodityClassificationService.delete(id);
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody CommodityClassificationNode commodityClassification) {
        return commodityClassificationService.update(commodityClassification);
    }
}
