package xin.admin.service.membershipManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.membershipManagement.UserDealType;
import xin.admin.mapper.membershipManagement.UserDealTypeMapper;
import xin.admin.service.membershipManagement.UserDealTypeService;

import java.util.List;

@Service
public class UserDealTypeServiceImpl implements UserDealTypeService {

    @Autowired
    private UserDealTypeMapper userDealTypeMapper;

    @Override
    public List<UserDealType> selectAll() {
        return userDealTypeMapper.selectList(null);
    }
}
