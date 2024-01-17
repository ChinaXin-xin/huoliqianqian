package xin.weixin.controller.myselfInfo.shippingAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.weixin.domain.myselfInfo.shippingAddress.ShippingAddress;
import xin.weixin.service.myselfInfo.shippingAddress.ShippingAddressService;

import java.util.List;

/**
 * 用户地址信息请求处理
 */
@RestController
@RequestMapping("/pro/myselfInformation")
public class ShippingAddressController {

    @Autowired
    private ShippingAddressService shippingAddressService;

    // 单一地址查找
    @PostMapping("/selectById/{id}")
    public ResponseResult<ShippingAddress> getInformation(@PathVariable Long id) {
        return shippingAddressService.selectById(id);
    }

    // 添加信息
    @PostMapping("/add")
    public ResponseResult addInformation(@RequestBody ShippingAddress entity) {
        return shippingAddressService.add(entity);
    }

    // 更新信息
    @PostMapping("/update")
    public ResponseResult updateInformation(@RequestBody ShippingAddress entity) {
        return shippingAddressService.update(entity);
    }

    // 删除信息
    @PostMapping("/deleteById/{id}")
    public ResponseResult deleteInformation(@PathVariable Long id) {
        return shippingAddressService.deleteById(id);
    }

    // 获取所有信息
    @PostMapping("/getAllInformation")
    public ResponseResult<List<ShippingAddress>> getAllInformation() {
        return shippingAddressService.getAllShippingAddresses();
    }
}
