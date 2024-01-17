package xin.admin.service.membershipManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;
import xin.admin.domain.membershipManagement.UserFees;
import xin.admin.domain.membershipManagement.UserWithdrawManagement;
import xin.admin.domain.membershipManagement.query.UserWithdrawManagementRequestQuery;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.membershipManagement.UserAuthenticationInfoMapper;
import xin.admin.mapper.membershipManagement.UserFeesMapper;
import xin.admin.mapper.membershipManagement.UserWithdrawManagementMapper;
import xin.admin.service.membershipManagement.UserWithdrawManagementService;
import xin.common.domain.User;
import xin.h5.domain.wallet.WithdrawDeposit;
import xin.h5.service.wallet.myEnum.AwardType;
import xin.yunhuo.api.YHV2Payment;
import xin.yunhuo.domain.PaymentRecord;
import xin.yunhuo.mapper.PaymentRecordMapper;
import xin.yunhuo.mapper.YunhuoResponseMsgMapper;

import java.util.Date;
import java.util.List;

@Service
public class UserWithdrawManagementServiceImpl implements UserWithdrawManagementService {
    @Autowired
    private UserWithdrawManagementMapper userWithdrawManagementMapper;

    @Autowired
    private UserAuthenticationInfoMapper userAuthenticationInfoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    UserFeesMapper userFeesMapper;

    @Autowired
    PaymentRecordMapper paymentRecordMapper;

    @Autowired
    YunhuoResponseMsgMapper yunhuoResponseMsgMapper;

    @Override
    public ResponseResult<List<UserWithdrawManagement>> selectAll() {
        List<UserWithdrawManagement> userWithdrawManagements = userWithdrawManagementMapper.selectAll();
        return new ResponseResult<>(200, "查询成功！", userWithdrawManagements);
    }

    @Override
    public ResponseResult<UserWithdrawManagementRequestQuery> select(UserWithdrawManagementRequestQuery query) {
        if (query == null) {
            return new ResponseResult<>(400, "请求参数不能为空", null);
        }

        // 设置默认的 pageNumber 和 quantity，例如 pageNumber=1，quantity=10
        Integer pageNumber = query.getPageNumber() == null ? 1 : query.getPageNumber();
        Integer quantity = query.getQuantity() == null ? 10 : query.getQuantity();

        // 修正 pageNumber 和 quantity 的值
        pageNumber = Math.max(pageNumber, 1);
        quantity = Math.max(quantity, 1);

        // 计算 OFFSET
        int offset = (pageNumber - 1) * quantity;

        System.out.println(query);
        List<UserWithdrawManagement> userTransactions = userWithdrawManagementMapper.selectPage(query.getUserWithdrawManagement(), offset, quantity);
        query.setResultList(userTransactions);
        if (query.getStartTime() == null || query.getEndTime() == null || query.getResultList() == null) {
            query.setCount(userWithdrawManagementMapper.selectPageCount(query.getUserWithdrawManagement(), offset, quantity));
        }
        // 封装到ResponseResult并返回
        return new ResponseResult<>(200, "查询成功", query);
    }

    @Override
    public ResponseResult alter(UserWithdrawManagement userWithdrawManagement) {
        userWithdrawManagement.setApprovalTime(new Date());
        userWithdrawManagementMapper.updateById(userWithdrawManagement);

        // 获取提现记录
        UserWithdrawManagement uwm = userWithdrawManagementMapper.mySelectByUid(userWithdrawManagement.getId());

        // 获取到提现人实名认证时的信息
        UserAuthenticationInfo userAuthenticationInfo = userAuthenticationInfoMapper.selectByUid((long) uwm.getUserAuthenticationInfoId());

        User curUser = userMapper.selectById(uwm.getUserAuthenticationInfoId());

        WithdrawDeposit withdrawDeposit = new WithdrawDeposit();
        withdrawDeposit.setMoney(uwm.getMoney());  //单位，分
        withdrawDeposit.setAwardType(AwardType.fromString(uwm.getWithdrawType()));

        if (userWithdrawManagement.getStatus() == 1) {

            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setMerOrderNo(String.valueOf(System.nanoTime())); // 商户订单号
            paymentRecord.setInAcctName(userAuthenticationInfo.getMemberName());                          // 收款人姓名
            paymentRecord.setInCidno(userAuthenticationInfo.getIdCardNumber());                // 收款人身份证号
            paymentRecord.setInMobile(userAuthenticationInfo.getMemberPhone());                      // 收款人手机
            paymentRecord.setInAcctNo(userAuthenticationInfo.getBankCard());              // 收款人银行卡号
            paymentRecord.setAmount(1L);                                   // 打款金额
            paymentRecord.setRemark("userid: " + uwm.getUserAuthenticationInfoId() + " " + uwm.getWithdrawType()); // 打款备注
            paymentRecord.setId_card_front(userAuthenticationInfo.getIdCardFront());                            // 提现人的身份证正面
            paymentRecord.setId_card_back(userAuthenticationInfo.getIdCardBack());                             // 提现人的身份证反面
            paymentRecord.setCidAddress("未知");                        // 收款人地址

            //系统手续费
            UserFees userFees = userFeesMapper.selectById(1);

            //用户要提现多少元，原来单位元，改为分
            float realityMoney = withdrawDeposit.getMoney();
            float money = 0; //对应奖项，一共有多少钱

            //手续费
            float serviceChargeProportion = 0;

            AwardType awardType = withdrawDeposit.getAwardType();
            if (awardType == AwardType.ACTIVATION_AWARD) {
                // 处理激活奖
                serviceChargeProportion = realityMoney * userFees.getActivationTaxFee();
                money = curUser.getReferralReward();

                realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

                //curUser.setActivationAward(curUser.getActivationAward() - withdrawDeposit.getMoney() * 100);
                userWithdrawManagement.setWithdrawType(AwardType.ACTIVATION_AWARD.toString());
            } else if (awardType == AwardType.ACTIVITY_AWARD) {
                // 处理活动奖
                serviceChargeProportion = realityMoney * userFees.getActivityTaxFee();
                money = curUser.getReferralReward();

                realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

                //curUser.setActivityAward(curUser.getActivityAward() - withdrawDeposit.getMoney() * 100);
                userWithdrawManagement.setWithdrawType(AwardType.ACTIVITY_AWARD.toString());
            } else if (awardType == AwardType.ACHIEVEMENT_AWARD) {
                // 处理达标奖
                serviceChargeProportion = realityMoney * userFees.getAchievementAward();
                money = curUser.getReferralReward();

                realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

                //curUser.setAchievementAward(curUser.getAchievementAward() - withdrawDeposit.getMoney() * 100);

                userWithdrawManagement.setWithdrawType(AwardType.ACHIEVEMENT_AWARD.toString());
            } else if (awardType == AwardType.TEAM_AWARD) {
                // 处理团队奖
                serviceChargeProportion = realityMoney * userFees.getTeamTaxFee();
                money = curUser.getReferralReward();

                realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

                //curUser.setTeamAward(curUser.getTeamAward() - withdrawDeposit.getMoney() * 100);

                userWithdrawManagement.setWithdrawType(AwardType.TEAM_AWARD.toString());
            } else if (awardType == AwardType.CHEER_AWARD) {
                // 处理加油奖
                serviceChargeProportion = realityMoney * userFees.getCheerAward();
                money = curUser.getReferralReward();

                realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

                //curUser.setCheerAward(curUser.getCheerAward() - withdrawDeposit.getMoney() * 100);

                userWithdrawManagement.setWithdrawType(AwardType.CHEER_AWARD.toString());
            } else if (awardType == AwardType.REFERRAL_REWARD) {
                // 处理推荐奖
                serviceChargeProportion = realityMoney * userFees.getReferralTaxFee();

                money = curUser.getReferralReward();

                realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

                //curUser.setReferralReward(curUser.getReferralReward() - withdrawDeposit.getMoney() * 100);

                userWithdrawManagement.setWithdrawType("推荐提现");
            } else if (awardType == AwardType.PROFIT_SHARING_REWARD) {
                // 处理分润奖
                serviceChargeProportion = realityMoney * userFees.getProfitSharingTaxFee();

                money = curUser.getProfitSharingReward();

                realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

                //curUser.setProfitSharingReward(curUser.getProfitSharingReward() - withdrawDeposit.getMoney() * 100);
                userWithdrawManagement.setWithdrawType(AwardType.PROFIT_SHARING_REWARD.toString());
            } else {
                return new ResponseResult<>(400, "请求参数不合法！");
            }

            float wd = (withdrawDeposit.getMoney() - (realityMoney - withdrawDeposit.getMoney()));

            StringBuilder result = new StringBuilder();
            result.append("确定提现到银行卡？</br>");
            result.append("姓名：" + userAuthenticationInfo.getMemberName() + "</br>");
            result.append("银行卡号：" + userAuthenticationInfo.getBankCard() + "</br>");
            result.append("扣除手续费：" + userFees.getWithdrawalFee() + "元，");
            result.append("扣除税点：" + serviceChargeProportion / 10000 + "元" + "</br>");
            result.append("扣除货款0元，" + "实际到账：" + wd + "元" + "</br>");

            paymentRecord.setAmount((long) wd);

            // 是否打款成功
            Boolean aBoolean = YHV2Payment.apiInvoke(paymentRecord);

            paymentRecordMapper.insert(paymentRecord);
            yunhuoResponseMsgMapper.insert(paymentRecord.getYunhuoResponseMsg());
            // 打款
            if (aBoolean) {
                return new ResponseResult(200, "提现申请成功！");
            } else {
                return new ResponseResult(400, "提现申请失败，云获平台响应：", paymentRecord.getYunhuoResponseMsg());
            }
        } else if (userWithdrawManagement.getStatus() == 2) {
            AwardType awardType = withdrawDeposit.getAwardType();
            if (awardType == AwardType.ACTIVATION_AWARD) {
                curUser.setActivationAward(curUser.getActivationAward() + uwm.getMoney());
            } else if (awardType == AwardType.ACTIVITY_AWARD) {
                curUser.setActivityAward(curUser.getActivityAward() + uwm.getMoney());
            } else if (awardType == AwardType.ACHIEVEMENT_AWARD) {
                curUser.setAchievementAward(curUser.getAchievementAward() + uwm.getMoney());
            } else if (awardType == AwardType.TEAM_AWARD) {
                curUser.setTeamAward(curUser.getTeamAward() + uwm.getMoney());
            } else if (awardType == AwardType.CHEER_AWARD) {
                curUser.setCheerAward(curUser.getCheerAward() + uwm.getMoney());
            } else if (awardType == AwardType.REFERRAL_REWARD) {
                curUser.setReferralReward(curUser.getReferralReward() + uwm.getMoney());
            } else if (awardType == AwardType.PROFIT_SHARING_REWARD) {
                float a = curUser.getProfitSharingReward();
                float b = uwm.getMoney();
                curUser.setProfitSharingReward(a + b);
            }
            userMapper.updateById(curUser);
            return new ResponseResult(200, "拒绝成功！");
        } else {
            return new ResponseResult(400, "修改失败！");
        }
    }
}
