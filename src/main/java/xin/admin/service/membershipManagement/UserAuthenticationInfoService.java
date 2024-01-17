package xin.admin.service.membershipManagement;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;
import xin.admin.domain.membershipManagement.query.UserAuthenticationInfoRequestQuery;

public interface UserAuthenticationInfoService {
    ResponseResult<UserAuthenticationInfoRequestQuery> list(UserAuthenticationInfoRequestQuery userAuthenticationInfoRequestQuery);

    ResponseResult alter(UserAuthenticationInfo userAuthenticationInfo);

    ResponseResult delete(Integer id);

    ResponseResult add(UserAuthenticationInfo userAuthenticationInfo);
}
