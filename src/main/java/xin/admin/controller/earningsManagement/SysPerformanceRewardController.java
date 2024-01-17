package xin.admin.controller.earningsManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.SysPerformanceRewardRequestQuery;
import xin.admin.service.earningsManagenment.SysPerformanceRewardService;

/**
 * 达标奖
 */
@RestController
@RequestMapping("/admin/earningsManagement/sysPerformanceReward")
public class SysPerformanceRewardController {
    @Autowired
    SysPerformanceRewardService sysPerformanceRewardService;

    @PostMapping("/list")
    public ResponseResult<SysPerformanceRewardRequestQuery> list(@RequestBody SysPerformanceRewardRequestQuery query) {
        return sysPerformanceRewardService.list(query);
    }
}
