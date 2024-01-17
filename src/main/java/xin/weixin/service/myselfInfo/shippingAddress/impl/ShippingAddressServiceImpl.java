package xin.weixin.service.myselfInfo.shippingAddress.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.common.domain.User;
import xin.weixin.domain.myselfInfo.shippingAddress.ShippingAddress;
import xin.weixin.mapper.myselfInfo.shippingAddress.ShippingAddressMapper;
import xin.weixin.service.myselfInfo.shippingAddress.ShippingAddressService;

import java.util.List;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {

    @Autowired
    private ShippingAddressMapper shippingAddressMapper;

    @Override
    public ResponseResult<ShippingAddress> selectById(Long id) {
        ShippingAddress shippingAddress = shippingAddressMapper.selectById(id);
        return new ResponseResult<>(200, "查询成功", shippingAddress);
    }

    @Override
    public ResponseResult<Void> add(ShippingAddress shippingAddress) {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        QueryWrapper<ShippingAddress> qw = new QueryWrapper<>();
        qw.eq("user_id", curUser.getId());
        List<ShippingAddress> list = shippingAddressMapper.selectList(qw);
        if (list.size() == 0) {
            shippingAddress.setIsDefault(1);
        }
        shippingAddress.setUserId(curUser.getId());

        shippingAddressMapper.insert(shippingAddress);
        return new ResponseResult<>(200, "添加成功");
    }

    @Override
    public ResponseResult<Void> update(ShippingAddress shippingAddress) {

        if (shippingAddress.getIsDefault() != null) {
            List<ShippingAddress> shippingAddresses = shippingAddressMapper.selectList(null);
            for (ShippingAddress sa : shippingAddresses) {
                if (sa.getIsDefault() == 1) {
                    sa.setIsDefault(0);
                    shippingAddressMapper.updateById(sa);
                }
            }
        }

        if (shippingAddressMapper.updateById(shippingAddress) > 0) {
            return new ResponseResult<>(200, "更新成功！");
        }
        return new ResponseResult<>(400, "更新失敗！");
    }

    @Override
    public ResponseResult<Void> deleteById(Long id) {
        if (shippingAddressMapper.deleteById(id) > 0) {
            return new ResponseResult<>(200, "删除成功！");
        }
        return new ResponseResult<>(400, "删除失敗！");
    }

    @Override
    public ResponseResult<List<ShippingAddress>> getAllShippingAddresses() {

        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        QueryWrapper<ShippingAddress> qw = new QueryWrapper<>();
        qw.eq("user_id", curUser.getId());
        qw.orderByDesc("is_default");
        List<ShippingAddress> list = shippingAddressMapper.selectList(qw);
        return new ResponseResult<>(200, "获取所有地址成功", list);
    }
}

