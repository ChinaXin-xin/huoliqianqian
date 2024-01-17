package xin.weixinBackground.service.commodityDetails.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.weixinBackground.domain.commodityDetails.CommodityClassificationNode;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;
import xin.weixinBackground.domain.commodityDetails.CommoditySpecification;
import xin.common.domain.CommonalityQuery;
import xin.weixinBackground.mapper.commodityDetails.CommodityDetailsMapper;
import xin.weixinBackground.mapper.commodityDetails.CommoditySpecificationMapper;
import xin.weixinBackground.service.commodityDetails.CommodityClassificationService;
import xin.weixinBackground.service.commodityDetails.CommodityDetailsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommodityDetailsServiceImpl implements CommodityDetailsService {

    @Autowired
    CommodityDetailsMapper commodityDetailsMapper;

    @Autowired
    CommoditySpecificationMapper commoditySpecificationMapper;

    @Autowired
    CommodityClassificationService commodityClassificationService;

    @Override
    public ResponseResult add(CommodityDetails commodityDetails) {

        if (commodityDetails != null
                && commodityDetails.getCommodityClassificationNodeIdList() != null
                && commodityDetails.getCommodityClassificationNodeIdList().size() != 0) {
            commodityDetails.setCommodityClassificationNodeId(
                    commodityDetails.getCommodityClassificationNodeIdList().get(
                            commodityDetails.getCommodityClassificationNodeIdList().size() - 1));
        }

        commodityDetailsMapper.insert(commodityDetails);
        if (commodityDetails.getCommoditySpecificationList() == null) {
            return new ResponseResult(400, "添加失败！");
        }
        for (CommoditySpecification c : commodityDetails.getCommoditySpecificationList()) {
            c.setCommodityDetailsId(commodityDetails.getId());
            commoditySpecificationMapper.insert(c);
        }
        return new ResponseResult(200, "添加成功！");
    }

    @Override
    public ResponseResult<CommodityDetails> selectById(Integer id) {
        CommodityDetails commodityDetails = commodityDetailsMapper.selectById(id);

        List<CommodityClassificationNode> allParentClassifications = commodityClassificationService.getAllParentClassifications(commodityDetails.getCommodityClassificationNodeId());

        commodityDetails.setCommodityClassificationNodeIdList(new ArrayList<>());

        // 把商品列表的id， 传过去，例如：111->222->333 查询333，把[222，333]的id以这种顺序传过去
        for (CommodityClassificationNode c : allParentClassifications) {
            commodityDetails.getCommodityClassificationNodeIdList().add(c.getId());
        }

        QueryWrapper<CommoditySpecification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commodity_details_id", id);
        commodityDetails.setCommoditySpecificationList(commoditySpecificationMapper.selectList(queryWrapper));

        return new ResponseResult<>(200, "查询成功！", commodityDetails);
    }

    @Override
    public ResponseResult<List<CommodityDetails>> selectByDetails(String details) {

        QueryWrapper<CommodityDetails> qw = new QueryWrapper<>();
        qw.like("details", details);

        List<CommodityDetails> commodityDetailList = commodityDetailsMapper.selectList(qw);

        for (CommodityDetails commodityDetails : commodityDetailList) {
            List<CommodityClassificationNode> allParentClassifications = commodityClassificationService.getAllParentClassifications(commodityDetails.getCommodityClassificationNodeId());

            commodityDetails.setCommodityClassificationNodeIdList(new ArrayList<>());

            // 把商品列表的id， 传过去，例如：111->222->333 查询333，把[222，333]的id以这种顺序传过去
            for (CommodityClassificationNode c : allParentClassifications) {
                commodityDetails.getCommodityClassificationNodeIdList().add(c.getId());
            }

            QueryWrapper<CommoditySpecification> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("commodity_details_id", commodityDetails.getId());
            commodityDetails.setCommoditySpecificationList(commoditySpecificationMapper.selectList(queryWrapper));
        }

        return new ResponseResult<>(200, "查询成功！", commodityDetailList);
    }

    @Override
    public ResponseResult delete(Integer id) {
        CommodityDetails commodityDetails = commodityDetailsMapper.selectById(id);

        List<CommodityClassificationNode> allParentClassifications = commodityClassificationService.getAllParentClassifications(commodityDetails.getCommodityClassificationNodeId());

        commodityDetails.setCommodityClassificationNodeIdList(new ArrayList<>());

        // 把商品列表的id， 传过去，例如：111->222->333 查询333，把[222，333]的id以这种顺序传过去
        for (CommodityClassificationNode c : allParentClassifications) {
            commodityDetails.getCommodityClassificationNodeIdList().add(c.getId());
        }

        QueryWrapper<CommoditySpecification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commodity_details_id", id);
        commodityDetails.setCommoditySpecificationList(commoditySpecificationMapper.selectList(queryWrapper));

        for (CommoditySpecification c : commodityDetails.getCommoditySpecificationList()) {
            commoditySpecificationMapper.deleteById(c);
        }

        commodityDetailsMapper.deleteById(commodityDetails);

        return new ResponseResult<>(200, "删除成功！");
    }

    @Override
    public ResponseResult<List<CommodityDetails>> selectByCommodityClassificationNodeId(Integer id) {

        QueryWrapper<CommodityDetails> qw = new QueryWrapper<>();
        qw.eq("commodity_classification_node_id", id);

        List<CommodityDetails> commodityDetails = commodityDetailsMapper.selectList(qw);

        for (CommodityDetails cd : commodityDetails) {
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
        return new ResponseResult<>(200, "查询成功！", commodityDetails);
    }

    @Override
    public ResponseResult<CommonalityQuery<CommodityDetails>> select(CommonalityQuery<CommodityDetails> query) {

        Page<CommodityDetails> page = new Page<>(query.getPageNumber(), query.getQuantity());

        QueryWrapper<CommodityDetails> qw = new QueryWrapper<>();
        qw.orderByDesc("id");
        if (query.getQuery() != null && query.getQuery().getDetails() != null) {
            qw.like("details", query.getQuery().getDetails());
        }


        Page<CommodityDetails> pageResult = commodityDetailsMapper.selectPage(page, qw);
        query.setResultList(pageResult.getRecords());

        for (CommodityDetails commodityDetails : query.getResultList()) {
            List<CommodityClassificationNode> allParentClassifications = commodityClassificationService.getAllParentClassifications(commodityDetails.getCommodityClassificationNodeId());

            commodityDetails.setCommodityClassificationNodeIdList(new ArrayList<>());

            // 把商品列表的id， 传过去，例如：111->222->333 查询333，把[222，333]的id以这种顺序传过去
            for (CommodityClassificationNode c : allParentClassifications) {
                commodityDetails.getCommodityClassificationNodeIdList().add(c.getId());
            }

            QueryWrapper<CommoditySpecification> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("commodity_details_id", commodityDetails.getId());
            commodityDetails.setCommoditySpecificationList(commoditySpecificationMapper.selectList(queryWrapper));
        }

        query.setCount(pageResult.getTotal());

        return new ResponseResult<>(200, "查询成功！", query);
    }

    @Override
    public ResponseResult update(CommodityDetails commodityDetails) {

        if (commodityDetails != null
                && commodityDetails.getCommodityClassificationNodeIdList() != null
                && commodityDetails.getCommodityClassificationNodeIdList().size() != 0) {
            commodityDetails.setCommodityClassificationNodeId(
                    commodityDetails.getCommodityClassificationNodeIdList().get(
                            commodityDetails.getCommodityClassificationNodeIdList().size() - 1));
        }

        commodityDetailsMapper.updateById(commodityDetails);
        if (commodityDetails.getCommoditySpecificationList() == null) {
            return new ResponseResult(400, "更新失败！");
        }
        for (CommoditySpecification c : commodityDetails.getCommoditySpecificationList()) {
            c.setCommodityDetailsId(commodityDetails.getId());
            commoditySpecificationMapper.updateById(c);
        }
        return new ResponseResult(200, "更新成功！");
    }
}
