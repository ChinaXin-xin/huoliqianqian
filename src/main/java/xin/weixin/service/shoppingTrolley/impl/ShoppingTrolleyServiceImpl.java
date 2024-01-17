package xin.weixin.service.shoppingTrolley.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.common.domain.User;
import xin.weixin.domain.shoppingTrolley.ShoppingTrolley;
import xin.weixin.mapper.shoppingTrolley.ShoppingTrolleyMapper;
import xin.weixin.service.shoppingTrolley.ShoppingTrolleyService;
import xin.weixinBackground.domain.commodityDetails.CommodityDetails;
import xin.weixinBackground.domain.commodityDetails.CommoditySpecification;
import xin.weixinBackground.mapper.commodityDetails.CommodityDetailsMapper;
import xin.weixinBackground.mapper.commodityDetails.CommoditySpecificationMapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class ShoppingTrolleyServiceImpl extends ServiceImpl<ShoppingTrolleyMapper, ShoppingTrolley> implements ShoppingTrolleyService {


    @Autowired
    CommodityDetailsMapper commodityDetailsMapper;

    @Autowired
    CommoditySpecificationMapper commoditySpecificationMapper;

    @Override
    public ResponseResult addShoppingTrolleyItem(ShoppingTrolley shoppingTrolley) {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        shoppingTrolley.setUserId(curUser.getId());
        shoppingTrolley.setCreateTime(new Date());
        boolean success = this.save(shoppingTrolley);
        if (success) {
            return new ResponseResult<>(200, "添加购物车项成功", null);
        } else {
            return new ResponseResult<>(400, "添加购物车项失败", null);
        }
    }

    @Override
    public ResponseResult deleteShoppingTrolleyItemById(Integer id) {
        boolean success = this.removeById(id);
        if (success) {
            return new ResponseResult<>(200, "删除购物车项成功", null);
        } else {
            return new ResponseResult<>(400, "删除购物车项失败", null);
        }
    }

    @Override
    public ResponseResult updateShoppingTrolleyItem(ShoppingTrolley shoppingTrolley) {
        boolean success = this.updateById(shoppingTrolley);
        if (success) {
            return new ResponseResult<>(200, "更新购物车项成功", null);
        } else {
            return new ResponseResult<>(400, "更新购物车项失败", null);
        }
    }

    @Override
    public ResponseResult<ShoppingTrolley> getShoppingTrolleyItemById(Integer id) {
        ShoppingTrolley item = this.getById(id);

        if (item != null) {
            CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectById(item.getSpecificationId());
            CommodityDetails commodityDetails = commodityDetailsMapper.selectById(commoditySpecification.getCommodityDetailsId());

            item.setCommoditySpecification(commoditySpecification);
            item.setCommodityDetails(commodityDetails);
            return new ResponseResult<>(200, "查询购物车项成功", item);
        } else {
            return new ResponseResult<>(400, "未找到购物车项", null);
        }
    }

    @Override
    public ResponseResult<List<ShoppingTrolley>> getAllShoppingTrolleyItems() {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", curUser.getId());

        List<ShoppingTrolley> items = this.listByMap(map);

        for (ShoppingTrolley item : items) {
            try {
                CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectById(item.getSpecificationId());
                CommodityDetails commodityDetails = commodityDetailsMapper.selectById(commoditySpecification.getCommodityDetailsId());

                item.setCommoditySpecification(commoditySpecification);
                item.setCommodityDetails(commodityDetails);
            } catch (Exception e) {
                log.warn("用户：{} 未找到购物车中对应商品编号：{}！", curUser.getId(), item.getSpecificationId());
            }
        }

        return new ResponseResult<>(200, "获取全部购物车项成功", items);
    }
}
