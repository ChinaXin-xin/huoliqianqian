package xin.admin.service.membershipManagement;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserWithdrawManagement;
import xin.admin.domain.membershipManagement.query.UserWithdrawManagementRequestQuery;

import java.util.List;

public interface UserWithdrawManagementService {
    ResponseResult<List<UserWithdrawManagement>> selectAll();

    ResponseResult<UserWithdrawManagementRequestQuery> select(UserWithdrawManagementRequestQuery query);

    ResponseResult alter(UserWithdrawManagement userWithdrawManagement);
}
