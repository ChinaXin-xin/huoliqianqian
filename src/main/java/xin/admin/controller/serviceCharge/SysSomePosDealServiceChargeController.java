package xin.admin.controller.serviceCharge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.fundsFlowManagement.query.SysPosTerminalRequestQuery;
import xin.admin.domain.serviceCharge.SysPosDealServiceCharge;
import xin.admin.domain.serviceCharge.SysSomePosDealServiceCharge;
import xin.admin.service.serviceCharge.SysSomePosDealServiceChargeService;

import java.util.List;

@RestController
@RequestMapping("/pro/SysSomePosDealServiceCharge")
public class SysSomePosDealServiceChargeController {

    @Autowired
    SysSomePosDealServiceChargeService sysSomePosDealServiceChargeService;

    @PostMapping("/select")
    public ResponseResult<SysPosTerminalRequestQuery> select(@RequestBody SysPosTerminalRequestQuery query) {
        return sysSomePosDealServiceChargeService.selectSysSomePosDealServiceCharge(query);
    }

    @PostMapping("/update")
    public ResponseResult<SysPosTerminalRequestQuery> setIsWhiteListStatus(@RequestBody SysPosTerminal spt) {
        return sysSomePosDealServiceChargeService.setIsWhiteListStatus(spt);
    }

    @PostMapping("/selectBySptId/{id}")
    public ResponseResult<List<SysPosDealServiceCharge>> selectBySptId(@PathVariable Integer id) {
        return sysSomePosDealServiceChargeService.selectBySptId(id);
    }

    @PostMapping("/setServiceCharge")
    public ResponseResult setServiceCharge(@RequestBody List<SysSomePosDealServiceCharge> list) {
        return sysSomePosDealServiceChargeService.setServiceCharge(list);
    }

    @PostMapping("/selectBySomeServiceCharge/{id}")
    public ResponseResult selectBySomeServiceCharge(@PathVariable Integer id) {
        return sysSomePosDealServiceChargeService.selectBySomeServiceCharge(id);
    }
}
