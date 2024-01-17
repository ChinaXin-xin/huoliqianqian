package xin.weixin.controller.shoppingTrolley;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.weixin.domain.shoppingTrolley.ShoppingTrolley;
import xin.weixin.service.shoppingTrolley.ShoppingTrolleyService;

import java.util.List;

@RestController
@RequestMapping("/pro/shoppingTrolley")
public class ShoppingTrolleyController {

    private final ShoppingTrolleyService shoppingTrolleyService;

    @Autowired
    public ShoppingTrolleyController(ShoppingTrolleyService shoppingTrolleyService) {
        this.shoppingTrolleyService = shoppingTrolleyService;
    }

    /**
     * 添加购物车项
     */
    @PostMapping("/add")
    public ResponseResult addShoppingTrolleyItem(@RequestBody ShoppingTrolley shoppingTrolley) {
        return shoppingTrolleyService.addShoppingTrolleyItem(shoppingTrolley);
    }

    /**
     * 获取所有购物车项
     */
    @PostMapping("/selectAll")
    public ResponseResult<List<ShoppingTrolley>> selectAll() {
        return shoppingTrolleyService.getAllShoppingTrolleyItems();
    }

    /**
     * 根据 ID 获取购物车项
     */
    @PostMapping("/selectById/{id}")
    public ResponseResult<ShoppingTrolley> selectById(@PathVariable Integer id) {
        return shoppingTrolleyService.getShoppingTrolleyItemById(id);
    }

    /**
     * 根据 ID 删除购物车项
     */
    @PostMapping("/deleteById/{id}")
    public ResponseResult deleteById(@PathVariable Integer id) {
        return shoppingTrolleyService.deleteShoppingTrolleyItemById(id);
    }

    /**
     * 更新购物车项
     */
    @PostMapping("/update")
    public ResponseResult updateShoppingTrolleyItem(@RequestBody ShoppingTrolley shoppingTrolley) {
        return shoppingTrolleyService.updateShoppingTrolleyItem(shoppingTrolley);
    }
}
