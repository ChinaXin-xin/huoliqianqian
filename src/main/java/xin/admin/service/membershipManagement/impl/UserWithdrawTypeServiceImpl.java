package xin.admin.service.membershipManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserWithdrawType;
import xin.admin.mapper.membershipManagement.UserWithdrawTypeMapper;
import xin.admin.service.membershipManagement.UserWithdrawTypeService;

import java.util.List;

@Service
public class UserWithdrawTypeServiceImpl implements UserWithdrawTypeService {

    @Autowired
    UserWithdrawTypeMapper userWithdrawTypeMapper;

    @Override
    public ResponseResult<List<UserWithdrawType>> selectAll() {
        return new ResponseResult<>(200, "查询成功！", userWithdrawTypeMapper.selectList(null));
    }
}
