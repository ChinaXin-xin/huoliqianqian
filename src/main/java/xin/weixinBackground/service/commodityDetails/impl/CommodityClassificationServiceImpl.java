package xin.weixinBackground.service.commodityDetails.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.weixinBackground.domain.commodityDetails.CommodityClassificationNode;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;
import xin.weixinBackground.domain.commodityDetails.CommoditySpecification;
import xin.weixinBackground.mapper.commodityDetails.CommodityClassificationMapper;
import xin.weixinBackground.mapper.commodityDetails.CommodityDetailsMapper;
import xin.weixinBackground.mapper.commodityDetails.CommoditySpecificationMapper;
import xin.weixinBackground.service.commodityDetails.CommodityClassificationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommodityClassificationServiceImpl implements CommodityClassificationService {

    @Autowired
    CommodityClassificationMapper commodityClassificationMapper;

    @Autowired
    CommodityDetailsMapper commodityDetailsMapper;

    @Autowired
    CommoditySpecificationMapper commoditySpecificationMapper;

    @Override
    public ResponseResult add(CommodityClassificationNode commodityClassification) {

        if (commodityClassificationMapper.insert(commodityClassification) > 0) {
            return new ResponseResult(200, "添加成功！");
        }

        return new ResponseResult(400, "添加失败！");
    }

    @Override
    public ResponseResult<CommodityClassificationNode> select() {
        List<CommodityClassificationNode> classifications = commodityClassificationMapper.selectList(null);
        Map<Integer, CommodityClassificationNode> map = new HashMap<>();

        // 将所有分类放入map，以便于快速查找
        for (CommodityClassificationNode node : classifications) {
            map.put(node.getId(), node);
        }

        CommodityClassificationNode root = null;
        for (CommodityClassificationNode node : classifications) {
            if (node.getParentId() == null || node.getParentId() == 0) {
                root = node; // 找到根节点
            } else {
                CommodityClassificationNode parent = map.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node); // 将节点加入其父节点的子节点列表中
                }
            }
        }
        return new ResponseResult<>(200, "查询成功！", root);
    }

    @Override
    public ResponseResult selectById(Integer id) {
        CommodityClassificationNode commodityClassificationNode = commodityClassificationMapper.selectById(id);
        if (commodityClassificationNode != null)
            return new ResponseResult(200, "查询成功！", commodityClassificationNode);
        return new ResponseResult(400, "查询失败！");
    }

    /**
     * 根据id，查询所有子分类与自己直属分类下的所有商品信息
     *
     * @return
     */
    @Override
    public ResponseResult<CommodityClassificationNode> selectByAllMsg(Integer id) {
        List<CommodityClassificationNode> classifications = commodityClassificationMapper.selectList(null);
        Map<Integer, CommodityClassificationNode> map = new HashMap<>();

        // 将所有分类放入map，以便于快速查找
        for (CommodityClassificationNode node : classifications) {
            map.put(node.getId(), node);
        }

        CommodityClassificationNode commodityClassificationNode = null;
        for (CommodityClassificationNode node : classifications) {
            if (node.getId().equals(id)) {
                commodityClassificationNode = node; // 找到根节点
            } else {
                CommodityClassificationNode parent = map.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node); // 将节点加入其父节点的子节点列表中
                }
            }
        }

        if (commodityClassificationNode != null) {

            QueryWrapper<CommodityDetails> qw = new QueryWrapper<>();
            qw.eq("commodity_classification_node_id", commodityClassificationNode.getId());

            // 规格下所有的商品
            List<CommodityDetails> commodityDetails = commodityDetailsMapper.selectList(qw);
            for (CommodityDetails cd : commodityDetails) {

                // 商品的所有规格
                List<CommoditySpecification> commoditySpecifications = commoditySpecificationMapper.selectByCommodityDetailsId(cd.getId());
                cd.setCommoditySpecificationList(commoditySpecifications);
            }
            commodityClassificationNode.setCommodityDetailsList(commodityDetails);
            return new ResponseResult(200, "查询成功！", commodityClassificationNode);
        }
        return new ResponseResult(400, "查询失败！");
    }

    @Override
    public ResponseResult delete(Integer id) {
        deleteChildrenRecursively(id);
        commodityClassificationMapper.deleteById(id); // 删除节点本身
        return new ResponseResult(200, "删除成功！");
    }

    @Override
    public ResponseResult update(CommodityClassificationNode commodityClassification) {
        if (commodityClassificationMapper.updateById(commodityClassification) > 0)
            return new ResponseResult(200, "修改成功！！");
        return new ResponseResult(400, "修改失败！");
    }

    @Override
    public List<CommodityClassificationNode> getAllParentClassifications(Integer id) {
        List<CommodityClassificationNode> parents = new ArrayList<>();
        CommodityClassificationNode sourceNode = commodityClassificationMapper.selectById(id);
        CommodityClassificationNode currentNode = sourceNode;


        while (currentNode != null && currentNode.getParentId() != null && currentNode.getParentId() != 0) {
            currentNode = commodityClassificationMapper.selectById(currentNode.getParentId());
            if (currentNode != null) {
                parents.add(0, currentNode); // 将父节点插入列表的开头
            }
        }
        parents.add(sourceNode);
        parents.remove(0);
        return parents;
    }

    // 删除所有子元素
    private void deleteChildrenRecursively(Integer parentId) {
        List<CommodityClassificationNode> children = commodityClassificationMapper.findByParentId(parentId);
        for (CommodityClassificationNode child : children) {
            deleteChildrenRecursively(child.getId()); // 递归删除子节点
            commodityClassificationMapper.deleteById(child.getId()); // 删除当前子节点
        }
    }
}
