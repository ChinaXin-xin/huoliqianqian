package xin.admin.service.fundsFlowManagement;

import xin.admin.controller.fundsFlowManagement.push.ZFInformPush;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.BindMachineInformZF;

public interface BindMachineInformZFService {
    ResponseResult add(ZFInformPush<BindMachineInformZF> zfInformPush);
}
