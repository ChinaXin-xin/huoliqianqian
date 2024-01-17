package xin.h5.service.wallet.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;
import xin.admin.domain.membershipManagement.UserFees;
import xin.admin.domain.membershipManagement.UserWithdrawManagement;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.membershipManagement.UserAuthenticationInfoMapper;
import xin.admin.mapper.membershipManagement.UserFeesMapper;
import xin.admin.mapper.membershipManagement.UserWithdrawManagementMapper;
import xin.common.domain.User;
import xin.h5.domain.wallet.SysAward;
import xin.h5.domain.wallet.WithdrawDeposit;
import xin.h5.mapper.wallet.SysAwardMapper;
import xin.h5.service.wallet.SysAwardService;
import xin.h5.service.wallet.myEnum.AwardType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Service
public class SysAwardServiceImpl implements SysAwardService {

    @Autowired
    SysAwardMapper sysAwardMapper;

    @Autowired
    UserAuthenticationInfoMapper userAuthenticationInfoMapper;

    @Autowired
    UserWithdrawManagementMapper userWithdrawManagementMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserFeesMapper userFeesMapper;

    //根据用户id，查询他的奖项是否能提现的信息
    @Override
    public ResponseResult<SysAward> queryUserAwardStatus(Long uid) {
        SysAward sysAward = sysAwardMapper.queryUserAwardStatus(uid);
        return new ResponseResult<>(200, "查询成功！", sysAward);
    }

    /**
     * 查询自己是否能提现的信息
     *
     * @return
     */
    @Override
    public ResponseResult<SysAward> queryMyselfAwardStatus() {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        SysAward sysAward = sysAwardMapper.queryUserAwardStatus(curUser.getId());
        return new ResponseResult<>(200, "查询成功！", sysAward);
    }

    //根据用户id，修改他的奖项是否能提现的信息
    @Override
    public ResponseResult<SysAward> updateUserAwardStatus(SysAward sysAward) {
        QueryWrapper<SysAward> sysAwardQueryWrapper = new QueryWrapper<>();
        sysAwardQueryWrapper.eq("uid", sysAward.getUid());
        if (sysAwardMapper.update(sysAward, sysAwardQueryWrapper) > 0) {
            return new ResponseResult<>(200, "更新成功！");
        }
        return new ResponseResult<>(200, "更新失败！");
    }

    @Override
    public ResponseResult<SysAward> queryAwardMoney(AwardType awardType) {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (!userAuthenticationInfoMapper.isAuthentication(curUser.getId())) {
            return new ResponseResult<>(403, "用户未实名认证");
        }

        SysAward sysAward = new SysAward();
        sysAward.setBankCard(userAuthenticationInfoMapper.selectByUid(curUser.getId()).getBankCard());
        sysAward.setPhone(userAuthenticationInfoMapper.selectByUid(curUser.getId()).getMemberPhone());

        if (awardType == AwardType.ACTIVATION_AWARD) {
            sysAward.setMoney(curUser.getActivationAward());
        } else if (awardType == AwardType.ACTIVITY_AWARD) {
            sysAward.setMoney(curUser.getActivityAward());
        } else if (awardType == AwardType.ACHIEVEMENT_AWARD) {
            sysAward.setMoney(curUser.getAchievementAward());
        } else if (awardType == AwardType.TEAM_AWARD) {
            sysAward.setMoney(curUser.getTeamAward());
        } else if (awardType == AwardType.CHEER_AWARD) {
            sysAward.setMoney(curUser.getCheerAward());
        } else if (awardType == AwardType.REFERRAL_REWARD) {
            sysAward.setMoney(curUser.getReferralReward());
        } else if (awardType == AwardType.PROFIT_SHARING_REWARD) {
            sysAward.setMoney(curUser.getProfitSharingReward());
        } else {
            return new ResponseResult<>(400, "请求参数不合法！");
        }

        return new ResponseResult<>(200, "查询成功！", sysAward);
    }

    //用户奖金提现申请
    @Override
    public ResponseResult withdrawDeposit(WithdrawDeposit withdrawDeposit) {

        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!userAuthenticationInfoMapper.isAuthentication(curUser.getId())) {
            return new ResponseResult<>(403, "用户未实名认证");
        }

        if (withdrawDeposit.getMoney() * 100 < 0.01) {
            return new ResponseResult<>(403, "提现金额不能小于0.01元");
        }

        //用户的实名信息
        UserAuthenticationInfo userAuthenticationInfo = userAuthenticationInfoMapper.selectByUid(curUser.getId());

        //系统手续费
        UserFees userFees = userFeesMapper.selectById(1);

        //提交申请
        UserWithdrawManagement userWithdrawManagement = new UserWithdrawManagement();

        //用户要提现多少元，原来单位元，改为分
        float realityMoney = withdrawDeposit.getMoney() * 100;
        float money = 0; //对应奖项，一共有多少钱

        //手续费
        float serviceChargeProportion = 0;

        AwardType awardType = withdrawDeposit.getAwardType();
        if (awardType == AwardType.ACTIVATION_AWARD) {
            // 处理激活奖
            serviceChargeProportion = realityMoney * userFees.getActivationTaxFee();
            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<激活奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

            curUser.setActivationAward(curUser.getActivationAward() - withdrawDeposit.getMoney() * 100);
            userWithdrawManagement.setWithdrawType(AwardType.ACTIVATION_AWARD.toString());
        } else if (awardType == AwardType.ACTIVITY_AWARD) {
            // 处理活动奖
            serviceChargeProportion = realityMoney * userFees.getActivityTaxFee();
            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<活动奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

            curUser.setActivityAward(curUser.getActivityAward() - withdrawDeposit.getMoney() * 100);
            userWithdrawManagement.setWithdrawType(AwardType.ACTIVITY_AWARD.toString());
        } else if (awardType == AwardType.ACHIEVEMENT_AWARD) {
            // 处理达标奖
            serviceChargeProportion = realityMoney * userFees.getAchievementAward();
            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<达标奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

            curUser.setAchievementAward(curUser.getAchievementAward() - withdrawDeposit.getMoney() * 100);

            userWithdrawManagement.setWithdrawType(AwardType.ACHIEVEMENT_AWARD.toString());
        } else if (awardType == AwardType.TEAM_AWARD) {
            // 处理团队奖
            serviceChargeProportion = realityMoney * userFees.getTeamTaxFee();
            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<团队奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

            curUser.setTeamAward(curUser.getTeamAward() - withdrawDeposit.getMoney() * 100);

            userWithdrawManagement.setWithdrawType(AwardType.TEAM_AWARD.toString());
        } else if (awardType == AwardType.CHEER_AWARD) {
            // 处理加油奖
            serviceChargeProportion = realityMoney * userFees.getCheerAward();
            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<加油奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

            curUser.setCheerAward(curUser.getCheerAward() - withdrawDeposit.getMoney() * 100);

            userWithdrawManagement.setWithdrawType(AwardType.CHEER_AWARD.toString());
        } else if (awardType == AwardType.REFERRAL_REWARD) {
            // 处理推荐奖
            serviceChargeProportion = realityMoney * userFees.getReferralTaxFee();

            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<推荐奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

            curUser.setReferralReward(curUser.getReferralReward() - withdrawDeposit.getMoney() * 100);

            userWithdrawManagement.setWithdrawType(AwardType.REFERRAL_REWARD.toString());
        } else if (awardType == AwardType.PROFIT_SHARING_REWARD) {
            // 处理分润奖
            serviceChargeProportion = realityMoney * userFees.getProfitSharingTaxFee();

            money = curUser.getProfitSharingReward();

            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<分润奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));

            curUser.setProfitSharingReward(curUser.getProfitSharingReward() - withdrawDeposit.getMoney() * 100);
            userWithdrawManagement.setWithdrawType(AwardType.PROFIT_SHARING_REWARD.toString());
        } else {
            return new ResponseResult<>(400, "请求参数不合法！");
        }

        if (realityMoney < 0) {
            return new ResponseResult<>(403, "提现金额与手续费，大于实际金额");
        }

        float wd = (withdrawDeposit.getMoney() - (realityMoney / 100 - withdrawDeposit.getMoney()));

        if (wd < 0.1) {
            return new ResponseResult<>(403, "提现金额小于手续费");
        }

        StringBuilder result = new StringBuilder();
        result.append("确定提现到银行卡？</br>");
        result.append("姓名：" + userAuthenticationInfo.getMemberName() + "</br>");
        result.append("银行卡号：" + userAuthenticationInfo.getBankCard() + "</br>");
        result.append("扣除手续费：" + userFees.getWithdrawalFee() + "元，");
        result.append("扣除税点：" + serviceChargeProportion / 10000 + "元" + "</br>");
        result.append("扣除货款0元，" + "实际到账：" + wd + "元" + "</br>");

        userWithdrawManagement.setUserAuthenticationInfoId((int) curUser.getId().longValue());
        userWithdrawManagement.setStatus(0);
        userWithdrawManagement.setWithdrawTime(new Date());
        userWithdrawManagement.setApprovalTime(new Date());
        userWithdrawManagement.setMoney(withdrawDeposit.getMoney() * 100);  //提现金额，前端用的元，后端用的分为单位

        userMapper.updateById(curUser);

        if (userWithdrawManagementMapper.insert(userWithdrawManagement) > 0) {
            return new ResponseResult<>(200, "申请提交成功！");
        }
        return new ResponseResult<>(403, "申请提交失败！！");
    }

    /**
     * 支付前弹出的提示
     *
     * @param withdrawDeposit
     * @return
     */
    @Override
    public ResponseResult<String> withdrawDepositBefore(WithdrawDeposit withdrawDeposit) {

        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (!userAuthenticationInfoMapper.isAuthentication(curUser.getId())) {
            return new ResponseResult<>(403, "用户未实名认证");
        }

        //用户的实名信息
        UserAuthenticationInfo userAuthenticationInfo = userAuthenticationInfoMapper.selectByUid(curUser.getId());

        //系统手续费
        UserFees userFees = userFeesMapper.selectById(1);

        //用户要提现多少元，原来单位元，改为分
        float realityMoney = withdrawDeposit.getMoney() * 100;
        float money = 0; //对应奖项，一共有多少钱

        //手续费
        float serviceChargeProportion = 0;

        AwardType awardType = withdrawDeposit.getAwardType();
        if (awardType == AwardType.ACTIVATION_AWARD) {
            // 处理激活奖
            serviceChargeProportion = realityMoney * userFees.getActivationTaxFee();
            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<激活奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));
        } else if (awardType == AwardType.ACTIVITY_AWARD) {
            // 处理活动奖
            serviceChargeProportion = realityMoney * userFees.getActivityTaxFee();
            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<活动奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));
        } else if (awardType == AwardType.ACHIEVEMENT_AWARD) {
            // 处理达标奖
            serviceChargeProportion = realityMoney * userFees.getAchievementAward();
            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<达标奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));
        } else if (awardType == AwardType.TEAM_AWARD) {
            // 处理团队奖
            serviceChargeProportion = realityMoney * userFees.getTeamTaxFee();
            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<团队奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));
        } else if (awardType == AwardType.CHEER_AWARD) {
            // 处理加油奖
            serviceChargeProportion = realityMoney * userFees.getCheerAward();
            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<加油奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));
        } else if (awardType == AwardType.REFERRAL_REWARD) {
            // 处理推荐奖
            serviceChargeProportion = realityMoney * userFees.getReferralTaxFee();

            money = curUser.getReferralReward();
            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<推荐奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));
        } else if (awardType == AwardType.PROFIT_SHARING_REWARD) {
            // 处理分润奖
            serviceChargeProportion = realityMoney * userFees.getProfitSharingTaxFee();

            money = curUser.getProfitSharingReward();

            if (realityMoney > money) {
                return new ResponseResult<>(403, "您的<分润奖>不够提现，您的奖金为：：" + money / 100);
            }

            realityMoney = realityMoney + (userFees.getWithdrawalFee() * 100 + (serviceChargeProportion / 100f));
        } else {
            return new ResponseResult<>(400, "请求参数不合法！");
        }

        if (realityMoney < 0) {
            return new ResponseResult<>(403, "提现金额与手续费，大于实际金额");
        }

        float wd = (withdrawDeposit.getMoney() - (realityMoney / 100 - withdrawDeposit.getMoney()));

        if (wd < 0.1) {
            return new ResponseResult<>(403, "提现金额小于手续费");
        }

        StringBuilder result = new StringBuilder();
        result.append("确定提现到银行卡？</br>");
        result.append("姓名：" + userAuthenticationInfo.getMemberName() + "</br>");
        result.append("银行卡号：" + userAuthenticationInfo.getBankCard() + "</br>");

        BigDecimal withdrawalFee = new BigDecimal(userFees.getWithdrawalFee());
        withdrawalFee = withdrawalFee.setScale(2, RoundingMode.DOWN);
        result.append("扣除手续费：" + withdrawalFee + "元，");

        BigDecimal tax = new BigDecimal(serviceChargeProportion / 10000.0);
        tax = tax.setScale(2, RoundingMode.DOWN);
        result.append("扣除税点：" + tax + "元" + "</br>");

        BigDecimal actualAmount = new BigDecimal(wd);
        actualAmount = actualAmount.setScale(2, RoundingMode.DOWN);
        result.append("扣除货款0元，实际到账：" + actualAmount + "元</br>");

        return new ResponseResult<>(200, "查询成功！", result.toString());
    }
}
