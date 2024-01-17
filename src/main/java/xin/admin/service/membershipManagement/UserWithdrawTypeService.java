package xin.admin.service.membershipManagement;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserWithdrawType;

import java.util.List;

public interface UserWithdrawTypeService {
    ResponseResult<List<UserWithdrawType>> selectAll();
}
