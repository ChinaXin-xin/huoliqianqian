package xin.admin.service.earningsManagenment;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysTeamRewardRequestQuery;

public interface SysTeamRewardService {
    ResponseResult<SysTeamRewardRequestQuery> list(SysTeamRewardRequestQuery sysTeamRewardRequestQuery);
}
