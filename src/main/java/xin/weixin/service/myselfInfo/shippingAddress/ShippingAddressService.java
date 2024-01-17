package xin.weixin.service.myselfInfo.shippingAddress;

import xin.admin.domain.ResponseResult;
import xin.weixin.domain.myselfInfo.shippingAddress.ShippingAddress;

import java.util.List;

public interface ShippingAddressService {
    ResponseResult<ShippingAddress> selectById(Long id);
    ResponseResult<Void> add(ShippingAddress shippingAddress);
    ResponseResult<Void> update(ShippingAddress shippingAddress);
    ResponseResult<Void> deleteById(Long id);
    ResponseResult<List<ShippingAddress>> getAllShippingAddresses();
}
