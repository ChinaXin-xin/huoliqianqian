package xin.admin.controller.fundsFlowManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.fundsFlowManagement.add.SysPosTerminalAdd;
import xin.admin.domain.fundsFlowManagement.query.SysPosTerminalRequestQuery;
import xin.admin.service.fundsFlowManagement.SysPosTerminalService;

@RestController
@RequestMapping("/admin/SysPosTerminal")
public class SysPosTerminalController {

    @Autowired
    SysPosTerminalService service;

    @PostMapping("/list")
    public ResponseResult<SysPosTerminalRequestQuery> list(@RequestBody SysPosTerminalRequestQuery query) {
        return service.list(query);
    }

    @PostMapping("/delete")
    public ResponseResult list(@RequestBody SysPosTerminal sysPosTerminal) {
        return service.delete(sysPosTerminal);
    }

    @PostMapping("/deleteList")
    public ResponseResult deleteList(@RequestBody SysPosTerminal[] snList) {
        return service.deleteList(snList);
    }

    @PostMapping("/add")
    public ResponseResult add(@RequestBody SysPosTerminalAdd add) {
        return service.add(add);
    }

/*    @PostMapping("setCharge")
    public ResponseResult */

}
