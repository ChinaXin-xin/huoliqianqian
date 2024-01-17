package xin.admin.service.earningsManagenment;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysProfitSharingRewardRequestQuery;

public interface SysProfitSharingRewardService {
    ResponseResult<SysProfitSharingRewardRequestQuery> list(SysProfitSharingRewardRequestQuery sysProfitSharingRewardRequestQuery);
}
