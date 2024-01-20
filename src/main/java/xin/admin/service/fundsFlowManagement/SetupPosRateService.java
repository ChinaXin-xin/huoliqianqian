package xin.admin.service.fundsFlowManagement;

import org.springframework.web.bind.annotation.RequestBody;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SetupPosRate;

public interface SetupPosRateService {
    ResponseResult set(SetupPosRate setupPosRate);
    ResponseResult get(SetupPosRate setupPosRate);
}
