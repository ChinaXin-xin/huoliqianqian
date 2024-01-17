package xin.admin.controller.earningsManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysProfitSharingRewardRequestQuery;
import xin.admin.service.earningsManagenment.SysProfitSharingRewardService;

/**
 * 分润奖,完
 */
@RestController
@RequestMapping("/admin/earningsManagement/sysProfitSharingReward")
public class SysProfitSharingRewardController {
    @Autowired
    SysProfitSharingRewardService sysProfitSharingRewardService;

    @PostMapping("/list")
    public ResponseResult<SysProfitSharingRewardRequestQuery> list(@RequestBody SysProfitSharingRewardRequestQuery query) {
        return sysProfitSharingRewardService.list(query);
    }
}
