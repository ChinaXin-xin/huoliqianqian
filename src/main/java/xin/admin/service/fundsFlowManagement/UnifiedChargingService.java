package xin.admin.service.fundsFlowManagement;

import org.springframework.web.bind.annotation.RequestBody;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SetupPosRate;
import xin.zhongFu.model.req.merchActivity.MerchFeeEditReq;
import xin.zhongFu.model.resp.MerchFeeQueryResp;

public interface UnifiedChargingService {
    ResponseResult simCharging(SetupPosRate setupPosRate);

    ResponseResult posCharging(SetupPosRate setupPosRate);

    ResponseResult vipCharging(SetupPosRate setupPosRate);

    ResponseResult setD0SingleCashDrawalOrD0FeeRate(MerchFeeQueryResp merchFeeEditReq, Boolean status);

    ResponseResult setD0FeeRateAndD0SingleCash(MerchFeeQueryResp merchFeeQueryResp);

    // 根据商家号，查询pos机的费率
    ResponseResult<MerchFeeQueryResp> getPosRate(String merchId);

    // 根据商家号，和费率，设置pos机的费率
    ResponseResult<MerchFeeQueryResp> setPosRate(String merchId, MerchFeeEditReq merchFeeEditReq);

    ResponseResult setAssignPosD0SingleCashDrawal(MerchFeeQueryResp merchFeeQueryResp, Boolean status);

    ResponseResult setAssignPosD0SingleCashDrawalOrD0FeeRate(MerchFeeQueryResp merchFeeQueryResp);

    ResponseResult selectByPosIdToFeeRate(Integer posId);
}
