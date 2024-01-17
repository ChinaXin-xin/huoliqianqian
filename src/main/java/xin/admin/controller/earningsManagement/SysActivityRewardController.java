package xin.admin.controller.earningsManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysActivityRewardRequestQuery;
import xin.admin.service.earningsManagenment.SysActivityRewardService;

/**
 * 活动奖励表，完
 */
@RestController
@RequestMapping("/admin/earningsManagement/sysActivityReward")
public class SysActivityRewardController {
    @Autowired
    SysActivityRewardService sysActivityRewardService;

    @PostMapping("/list")
    public ResponseResult<SysActivityRewardRequestQuery> list(@RequestBody SysActivityRewardRequestQuery query) {
        return sysActivityRewardService.list(query);
    }
}
