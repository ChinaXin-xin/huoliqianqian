package xin.admin.service.earningsManagenment;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysPerformanceRewardRequestQuery;

public interface SysPerformanceRewardService {
    ResponseResult<SysPerformanceRewardRequestQuery> list(SysPerformanceRewardRequestQuery sysPerformanceRewardRequestQuery);
}
