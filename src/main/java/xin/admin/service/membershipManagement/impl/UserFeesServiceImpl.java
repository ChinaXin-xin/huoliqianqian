package xin.admin.service.membershipManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserFees;
import xin.admin.mapper.membershipManagement.UserFeesMapper;
import xin.admin.service.membershipManagement.UserFeesService;

@Service
public class UserFeesServiceImpl implements UserFeesService {
    @Autowired
    UserFeesMapper userFeesMapper;
    @Override
    public ResponseResult<UserFees> select() {
        return new ResponseResult<>(200,"查询成功！",userFeesMapper.selectById(1));
    }

    @Override
    public ResponseResult<UserFees> alter(UserFees userFees) {
        userFeesMapper.updateById(userFees);
        return new ResponseResult<>(200,"修改成功！");
    }
}
