package xin.h5.controller.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.h5.domain.wallet.SysAward;
import xin.h5.domain.wallet.WithdrawDeposit;
import xin.h5.service.wallet.SysAwardService;
import xin.h5.service.wallet.myEnum.AwardType;

@RestController
public class SysAwardController {

    @Autowired
    SysAwardService sysAwardService;

    @PostMapping("/admin/sysAward/queryUserAwardStatusAdmin/{uid}")
    ResponseResult<SysAward> queryUserAwardStatusAdmin(@PathVariable Long uid) {
        return sysAwardService.queryUserAwardStatus(uid);
    }

    @PostMapping("/h5/sysAward/queryUserAwardStatusH5")
    ResponseResult<SysAward> queryMyselfAwardStatus() {
        return sysAwardService.queryMyselfAwardStatus();
    }

    @PostMapping("/admin/sysAward/updateUserAwardStatus")
    ResponseResult<SysAward> updateUserAwardStatus(@RequestBody SysAward sysAward) {
        return sysAwardService.updateUserAwardStatus(sysAward);
    }

    //查询每个奖对应的money
    @PostMapping("/h5/sysAward/queryAwardMoney")
    ResponseResult<SysAward> queryAwardMoney(@RequestBody AwardType awardType) {
        return sysAwardService.queryAwardMoney(awardType);
    }

    //用户奖金提现申请
    @PostMapping("/h5/sysAward/withdrawDeposit")
    ResponseResult withdrawDeposit(@RequestBody WithdrawDeposit withdrawDeposit) {
        return sysAwardService.withdrawDeposit(withdrawDeposit);
    }

    /**
     * 支付前弹出的提示
     * @param withdrawDeposit
     * @return
     */
    @PostMapping("/h5/sysAward/withdrawDepositBefore")
    ResponseResult<String> withdrawDepositBefore(@RequestBody WithdrawDeposit withdrawDeposit) {
        return sysAwardService.withdrawDepositBefore(withdrawDeposit);
    }
}
