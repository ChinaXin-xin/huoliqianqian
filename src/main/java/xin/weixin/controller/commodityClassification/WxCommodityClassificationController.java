package xin.weixin.controller.commodityClassification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.weixinBackground.domain.commodityDetails.CommodityClassificationNode;
import xin.weixinBackground.service.commodityDetails.CommodityClassificationService;

/**
 * 商品分类
 */
@RestController
@RequestMapping("/pro/commodityClassification")
public class WxCommodityClassificationController {

    @Autowired
    CommodityClassificationService commodityClassificationService;

    @PostMapping("/select")
    public ResponseResult<CommodityClassificationNode> select() {
        return commodityClassificationService.select();
    }

    /**
     * 根据id，查询所有子分类与自己直属分类下的所有商品信息
     *
     * @return
     */
    @PostMapping("/selectByAllMsg/{id}")
    public ResponseResult<CommodityClassificationNode> selectByAllMsg(@PathVariable Integer id) {
        return commodityClassificationService.selectByAllMsg(id);
    }
}
