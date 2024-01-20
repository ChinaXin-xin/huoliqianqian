package xin.admin.controller.fundsFlowManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SetupPosRate;
import xin.admin.service.fundsFlowManagement.SetupPosRateService;

/**
 * 设置pos的费率
 */
@RestController
@RequestMapping("/admin/SetupPosRate")
public class SetupPosRateController {

    @Autowired
    SetupPosRateService setupPosRateService;

    @PostMapping("/set")
    public ResponseResult set(@RequestBody SetupPosRate setupPosRate) {
        return setupPosRateService.set(setupPosRate);
    }

    @PostMapping("/get")
    public ResponseResult get(@RequestBody SetupPosRate setupPosRate) {
        return setupPosRateService.get(setupPosRate);
    }
}
