package xin.admin.service.earningsManagenment;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysActivationRewardRequestQuery;

public interface SysActivationRewardService {
    ResponseResult<SysActivationRewardRequestQuery> list(SysActivationRewardRequestQuery sysActivationRewardRequestQuery);
}
