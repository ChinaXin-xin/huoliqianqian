package xin.admin.controller.fundsFlowManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.controller.fundsFlowManagement.push.ZFInformPush;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.BindMachineInformZF;
import xin.admin.service.fundsFlowManagement.BindMachineInformZFService;

@RestController
@RequestMapping("/admin/bindMachineInformZF")
public class BindMachineInformZFController {

    @Autowired
    BindMachineInformZFService bindMachineInformZFService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody ZFInformPush<BindMachineInformZF> zfInformPush) {
        return bindMachineInformZFService.add(zfInformPush);
    }
}
