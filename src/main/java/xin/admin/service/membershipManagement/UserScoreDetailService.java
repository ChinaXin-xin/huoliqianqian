package xin.admin.service.membershipManagement;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserScoreDetail;
import xin.admin.domain.membershipManagement.query.UserScoreDetailRequestQuery;

public interface UserScoreDetailService {
    ResponseResult<UserScoreDetailRequestQuery> selectUserScoreDetailPage(UserScoreDetailRequestQuery userScoreDetailRequestQuery);

    ResponseResult add(UserScoreDetail userScoreDetail);
}
