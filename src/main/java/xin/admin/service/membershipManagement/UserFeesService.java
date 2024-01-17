package xin.admin.service.membershipManagement;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserFees;

public interface UserFeesService {
    ResponseResult<UserFees> select();
    ResponseResult<UserFees> alter(UserFees userFees);
}
