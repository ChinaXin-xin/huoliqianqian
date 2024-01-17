package xin.admin.service.membershipManagement;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.query.UserTransactionsRequestQuery;

public interface UserTransactionsService {
    ResponseResult<UserTransactionsRequestQuery> list(UserTransactionsRequestQuery query);
}
