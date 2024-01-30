package xin.admin.controller.fundsFlowManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysFeeRate;
import xin.admin.service.fundsFlowManagement.SysFeeRateService;

import java.util.List;

@RestController
@RequestMapping("/admin/sysFeeRate")
public class SysFeeRateController {

    @Autowired
    SysFeeRateService sysFeeRateService;

    @PostMapping("/list")
    ResponseResult<List<SysFeeRate>> list() {
        return sysFeeRateService.list();
    }

    @PostMapping("/updateSimFeeRatePeriod")
    ResponseResult setSimFeeRatePeriod(@RequestBody SysFeeRate simFee) {
        return sysFeeRateService.setSimFeeRatePeriod(simFee);
    }

    @PostMapping("/updatePosFeeRatePeriod")
    ResponseResult updatePosFeeRatePeriod(@RequestBody SysFeeRate simFee) {
        return sysFeeRateService.updatePosFeeRatePeriod(simFee);
    }

    @PostMapping("/updateVipFeeRatePeriod")
    ResponseResult updateVipFeeRatePeriod(@RequestBody SysFeeRate simFee) {
        return sysFeeRateService.updateVipFeeRatePeriod(simFee);
    }
}
