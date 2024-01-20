package xin.admin.controller.serviceCharge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.serviceCharge.SysPosDealServiceCharge;
import xin.admin.service.serviceCharge.SysPosDealServiceChargeService;

import java.util.List;

@RestController
@RequestMapping("/pro/sysPosDealServiceCharge")
public class SysPosDealServiceChargeController {

    private final SysPosDealServiceChargeService sysPosDealServiceChargeService;

    @Autowired
    public SysPosDealServiceChargeController(SysPosDealServiceChargeService sysPosDealServiceChargeService) {
        this.sysPosDealServiceChargeService = sysPosDealServiceChargeService;
    }

    @PostMapping("/add")
    public ResponseResult<Void> addSysPosDealServiceCharge(@RequestBody SysPosDealServiceCharge record) {
        return sysPosDealServiceChargeService.addSysPosDealServiceCharge(record);
    }

    @PostMapping("/deleteById/{id}")
    public ResponseResult<Void> deleteSysPosDealServiceCharge(@PathVariable Integer id) {
        return sysPosDealServiceChargeService.deleteSysPosDealServiceCharge(id);
    }

    @PostMapping("/selectById/{id}")
    public ResponseResult<SysPosDealServiceCharge> selectSysPosDealServiceCharge(@PathVariable Integer id) {
        return sysPosDealServiceChargeService.selectByIdSysPosDealServiceCharge(id);
    }

    @PostMapping("/update")
    public ResponseResult<Void> updateSysPosDealServiceCharge(@RequestBody SysPosDealServiceCharge record) {
        return sysPosDealServiceChargeService.updateSysPosDealServiceCharge(record);
    }

    @PostMapping("/select")
    public ResponseResult<List<SysPosDealServiceCharge>> findById() {
        return sysPosDealServiceChargeService.selectSysPosDealServiceCharge();
    }
}
