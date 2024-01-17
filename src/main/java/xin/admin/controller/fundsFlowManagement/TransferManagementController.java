package xin.admin.controller.fundsFlowManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.Transfer;
import xin.admin.domain.fundsFlowManagement.query.SysPosTerminalRequestQuery;
import xin.admin.service.fundsFlowManagement.SysPosTerminalService;

@RestController
@RequestMapping("/admin/transferManagement")
public class TransferManagementController {

    @Autowired
    SysPosTerminalService service;

    @PostMapping("/list")
    public ResponseResult<SysPosTerminalRequestQuery> list(@RequestBody SysPosTerminalRequestQuery query) {
        return service.listTransferManagement(query);
    }

    @PostMapping("/transfer")
    public ResponseResult transfer(@RequestBody Transfer transfer) {
        return service.transfer(transfer);
    }

    @PostMapping("/transferList")
    public ResponseResult transferList(@RequestBody Transfer transfer) {
        return service.transferList(transfer);
    }


    @PostMapping("/transferToAdmin")
    ResponseResult transferToAdmin(@RequestBody Transfer transfer) {
        return service.transferToAdmin(transfer);
    }
}
