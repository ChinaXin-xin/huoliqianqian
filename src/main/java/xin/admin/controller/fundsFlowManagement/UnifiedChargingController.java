package xin.admin.controller.fundsFlowManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SetupPosRate;
import xin.admin.service.fundsFlowManagement.UnifiedChargingService;
import xin.zhongFu.model.resp.MerchFeeQueryResp;

/**
 * 统一收费接口
 */
@RestController
@RequestMapping("/admin/unifiedCharging")
public class UnifiedChargingController {

    @Autowired
    UnifiedChargingService unifiedChargingService;

    /**
     * 同一收取流量费
     *
     * @param setupPosRate
     * @return
     */
    @PostMapping("/simCharging")
    public ResponseResult simCharging(@RequestBody SetupPosRate setupPosRate) {
        return unifiedChargingService.simCharging(setupPosRate);
    }

    /**
     * 同一收取押金
     *
     * @param setupPosRate
     * @return
     */
    @PostMapping("/posCharging")
    public ResponseResult posCharging(@RequestBody SetupPosRate setupPosRate) {
        return unifiedChargingService.posCharging(setupPosRate);
    }

    /**
     * 同一收取会员费
     *
     * @param setupPosRate
     * @return
     */
    @PostMapping("/vipCharging")
    public ResponseResult vipCharging(@RequestBody SetupPosRate setupPosRate) {
        return unifiedChargingService.vipCharging(setupPosRate);
    }

    /**
     * 同一收设置：D0单笔提现元（单笔交易服务费）
     *
     * @param merchFeeQueryResp
     * @return
     */
    @PostMapping("/setD0SingleCashDrawal")
    public ResponseResult setD0SingleCashDrawal(@RequestBody MerchFeeQueryResp merchFeeQueryResp) {
        return unifiedChargingService.setD0SingleCashDrawalOrD0FeeRate(merchFeeQueryResp, false);
    }

    /**
     * 同一收设置：D0手续费费率
     * 同一收设置：D0单笔提现元（单笔交易服务费）
     * 两个结合了
     * 3%为：0.03
     *
     * @param merchFeeQueryResp
     * @return
     */
    @PostMapping("/setD0FeeRateAndD0SingleCash")
    public ResponseResult setD0FeeRateAndD0SingleCash(@RequestBody MerchFeeQueryResp merchFeeQueryResp) {
        return unifiedChargingService.setD0FeeRateAndD0SingleCash(merchFeeQueryResp);
    }

    /**
     * 同一收设置：D0手续费费率
     * 3%为：0.03
     *
     * @param merchFeeQueryResp
     * @return
     */
    @PostMapping("/setD0FeeRate")
    public ResponseResult setD0FeeRate(@RequestBody MerchFeeQueryResp merchFeeQueryResp) {
        return unifiedChargingService.setD0SingleCashDrawalOrD0FeeRate(merchFeeQueryResp, true);
    }


    /**
     * 设置某个pos机的D0单笔提现元（单笔交易服务费）
     *
     * @param merchFeeQueryResp
     * @return
     */
    @PostMapping("/setAssignPosD0SingleCashDrawal")
    public ResponseResult setAssignPosD0SingleCashDrawal(@RequestBody MerchFeeQueryResp merchFeeQueryResp) {
        return unifiedChargingService.setAssignPosD0SingleCashDrawal(merchFeeQueryResp, false);
    }

    /**
     * 设置某个pos机的D0手续费费率(%)
     * @param merchFeeQueryResp
     * @return
     */
    @PostMapping("/setAssignPosD0FeeRate")
    public ResponseResult setAssignPosD0FeeRate(@RequestBody MerchFeeQueryResp merchFeeQueryResp) {
        return unifiedChargingService.setAssignPosD0SingleCashDrawal(merchFeeQueryResp, true);
    }

    /**
     * 设置某个pos机的D0手续费费率(%)设置某个pos机的D0单笔提现元（单笔交易服务费）
     * setD0SingleCashDrawalOrD0FeeRate()和setD0FeeRate()的结合体
     * @param merchFeeQueryResp
     * @return
     */
    @PostMapping("/setAssignPosD0SingleCashDrawalOrD0FeeRate")
    public ResponseResult setAssignPosD0SingleCashDrawalOrD0FeeRate(@RequestBody MerchFeeQueryResp merchFeeQueryResp) {
        return unifiedChargingService.setAssignPosD0SingleCashDrawalOrD0FeeRate(merchFeeQueryResp);
    }

    /**
     * 根据posId查询费率
     */
    @PostMapping("/selectByPosIdToFeeRate/{posId}")
    public ResponseResult selectByPosIdToFeeRate(@PathVariable Integer posId) {
        return unifiedChargingService.selectByPosIdToFeeRate(posId);
    }
}
