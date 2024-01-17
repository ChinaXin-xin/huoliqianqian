package xin.admin.controller.earningsManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysReferralRewardRequestQuery;
import xin.admin.service.earningsManagenment.SysReferralRewardService;

/**
 * 推荐奖，完
 */
@RestController
@RequestMapping("/admin/earningsManagement/sysReferralReward")
public class SysReferralRewardController {
    @Autowired
    SysReferralRewardService sysReferralRewardService;

    @PostMapping("/list")
    public ResponseResult<SysReferralRewardRequestQuery> list(@RequestBody SysReferralRewardRequestQuery query) {
        return sysReferralRewardService.list(query);
    }
}
