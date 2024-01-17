package xin.admin.service.earningsManagenment;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.UserScoreRequestQuery;

public interface UserScoreService {
    ResponseResult<UserScoreRequestQuery> list(UserScoreRequestQuery query);
}
