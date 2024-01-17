package xin.admin.service.earningsManagenment;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysReferralRewardRequestQuery;

public interface SysReferralRewardService {
    ResponseResult<SysReferralRewardRequestQuery> list(SysReferralRewardRequestQuery sysReferralRewardRequestQuery);
}
