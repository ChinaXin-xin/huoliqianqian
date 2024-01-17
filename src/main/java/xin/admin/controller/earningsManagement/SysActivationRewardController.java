package xin.admin.controller.earningsManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysActivationRewardRequestQuery;
import xin.admin.service.earningsManagenment.SysActivationRewardService;

/**
 * 激活奖，完
 */
@RestController
@RequestMapping("/admin/earningsManagement/sysActivationReward")
public class SysActivationRewardController {
    @Autowired
    SysActivationRewardService sysActivationRewardService;

    @PostMapping("/list")
    public ResponseResult<SysActivationRewardRequestQuery> list(@RequestBody SysActivationRewardRequestQuery query) {
        return sysActivationRewardService.list(query);
    }
}
