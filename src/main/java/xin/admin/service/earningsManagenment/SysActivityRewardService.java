package xin.admin.service.earningsManagenment;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysActivityRewardRequestQuery;

public interface SysActivityRewardService {
    ResponseResult<SysActivityRewardRequestQuery> list(SysActivityRewardRequestQuery sysActivityRewardRequestQuery);
}
