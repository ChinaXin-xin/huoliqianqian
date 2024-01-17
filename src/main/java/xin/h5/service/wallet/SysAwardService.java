package xin.h5.service.wallet;

import xin.admin.domain.ResponseResult;
import xin.h5.domain.wallet.SysAward;
import xin.h5.domain.wallet.WithdrawDeposit;
import xin.h5.service.wallet.myEnum.AwardType;

public interface SysAwardService {
    //根据用户id，查询他的奖项是否能提现的信息
    ResponseResult<SysAward> queryUserAwardStatus(Long uid);

    //查询自己的奖项是否能提现的信息
    ResponseResult<SysAward> queryMyselfAwardStatus();

    //根据用户id，修改他的奖项是否能提现的信息
    ResponseResult<SysAward> updateUserAwardStatus(SysAward uid);

    //查询每个奖对应的money
    ResponseResult<SysAward> queryAwardMoney(AwardType awardType);

    //用户提现申请
    ResponseResult withdrawDeposit(WithdrawDeposit withdrawDeposit);

    /**
     * 支付前弹出的提示
     * @param withdrawDeposit
     * @return
     */
    ResponseResult<String> withdrawDepositBefore(WithdrawDeposit withdrawDeposit);
}
