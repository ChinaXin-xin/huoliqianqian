package xin.weixin.service.shoppingTrolley;

import com.baomidou.mybatisplus.extension.service.IService;
import xin.admin.domain.ResponseResult;
import xin.weixin.domain.shoppingTrolley.ShoppingTrolley;

import java.util.List;

public interface ShoppingTrolleyService extends IService<ShoppingTrolley> {

    // 添加一个购物车项
    ResponseResult addShoppingTrolleyItem(ShoppingTrolley shoppingTrolley);

    // 根据ID删除购物车项
    ResponseResult deleteShoppingTrolleyItemById(Integer id);

    // 更新购物车项
    ResponseResult updateShoppingTrolleyItem(ShoppingTrolley shoppingTrolley);

    // 根据ID查询购物车项
    ResponseResult<ShoppingTrolley> getShoppingTrolleyItemById(Integer id);

    // 获取所有购物车项
    ResponseResult<List<ShoppingTrolley>> getAllShoppingTrolleyItems();
}
