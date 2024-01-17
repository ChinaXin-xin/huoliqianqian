package xin.admin.controller.earningsManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysTeamRewardRequestQuery;
import xin.admin.service.earningsManagenment.SysTeamRewardService;

/**
 * 团队奖，完
 */
@RestController
@RequestMapping("/admin/earningsManagement/sysTeamReward")
public class SysTeamRewardController {
    @Autowired
    SysTeamRewardService sysTeamRewardService;

    @PostMapping("/list")
    public ResponseResult<SysTeamRewardRequestQuery> list(@RequestBody SysTeamRewardRequestQuery query) {
        return sysTeamRewardService.list(query);
    }
}
