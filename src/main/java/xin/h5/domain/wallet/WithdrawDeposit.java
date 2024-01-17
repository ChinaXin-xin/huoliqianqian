package xin.h5.domain.wallet;

import lombok.Data;
import xin.h5.service.wallet.myEnum.AwardType;

@Data
public class WithdrawDeposit {

    //提现的类型
    private AwardType awardType;

    //提现的钱
    private Float money;
}
