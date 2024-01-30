package xin.admin.service.fundsFlowManagement;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysFeeRate;

import java.util.List;

public interface SysFeeRateService {
    ResponseResult<List<SysFeeRate>> list();

    ResponseResult setSimFeeRatePeriod(SysFeeRate simFee);
    ResponseResult updatePosFeeRatePeriod(SysFeeRate simFee);
    ResponseResult updateVipFeeRatePeriod(SysFeeRate simFee);
}
