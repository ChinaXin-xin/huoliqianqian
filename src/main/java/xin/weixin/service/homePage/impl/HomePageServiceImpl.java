package xin.weixin.service.homePage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.weixin.service.homePage.HomePageService;
import xin.weixinBackground.domain.commodityDetails.CommodityClassificationNode;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;
import xin.weixinBackground.domain.commodityDetails.CommoditySpecification;
import xin.weixinBackground.mapper.commodityDetails.CommodityDetailsMapper;
import xin.weixinBackground.mapper.commodityDetails.CommoditySpecificationMapper;
import xin.weixinBackground.service.commodityDetails.CommodityClassificationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    CommodityDetailsMapper commodityDetailsMapper;

    @Autowired
    CommoditySpecificationMapper commoditySpecificationMapper;

    @Autowired
    CommodityClassificationService commodityClassificationService;

    /**
     * 首页的好物推荐，商品随机10条
     */
    @Override
    public ResponseResult<List<CommodityDetails>> getRandomRecommendedItems() {
        List<CommodityDetails> randomItems = new ArrayList<>();

        for (CommodityDetails c : commodityDetailsMapper.selectRandomCommodities(10)) {;
            randomItems.add(commodityDetailsMapper.selectById(c.getId()));
        }

        for (CommodityDetails cd : randomItems) {
            List<CommodityClassificationNode> allParentClassifications = commodityClassificationService.getAllParentClassifications(cd.getCommodityClassificationNodeId());

            cd.setCommodityClassificationNodeIdList(new ArrayList<>());

            // 把商品列表的id， 传过去，例如：111->222->333 查询333，把[222，333]的id以这种顺序传过去
            for (CommodityClassificationNode c : allParentClassifications) {
                cd.getCommodityClassificationNodeIdList().add(c.getId());
            }

            QueryWrapper<CommoditySpecification> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("commodity_details_id", cd.getId());
            cd.setCommoditySpecificationList(commoditySpecificationMapper.selectList(queryWrapper));
        }

        return new ResponseResult<>(200, "查询成功！", randomItems);
    }
}
