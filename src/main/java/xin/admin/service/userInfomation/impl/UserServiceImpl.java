package xin.admin.service.userInfomation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.UserLevel;
import xin.admin.domain.fundsFlowManagement.CommercialTenantOrderZF;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.mapper.membershipManagement.UserLevelMapper;
import xin.admin.service.userInfomation.UserService;
import xin.common.domain.User;
import xin.level.service.UserGradationService;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserLevelMapper userLevelMapper;

    @Autowired
    UserGradationService userGradationService;

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    UserMapper userMapper;

    //最大的分润的钱
    private float maxShareBenefit = 0.0f;

    /**
     * 获取用户等级，如果手动设置了，就返回手动设置的
     *
     * @param curUser
     * @return
     */
    @Override
    public String getUserLevel(User curUser) {

        String userLevel = "";

        // 判断是否到期
        if (curUser.getExpireDate() != null && !curUser.getExpireDate().before(new Date()) && !curUser.getStartDate().after(new Date())) {
            //查询用户手动级别
            userLevel = userLevelMapper.selectByName(curUser.getManualLevel()).getName();
        } else {
            List<UserLevel> userLevelList = userLevelMapper.selectListOrderByASC();

            //防止没有数据
            if (userLevelList.size() < 2)
                userLevel = "V1";
            else {
                if (userLevelList.get(0).getUpgradeVolume() > curUser.getTransactionVolume()) {
                    userLevel = "V1";
                }
                for (int i = 1, size = userLevelList.size(); i < size; i++) {
                    if (userLevelList.get(i).getUpgradeVolume() > curUser.getTransactionVolume()) {
                        userLevel = userLevelList.get(i - 1).getName();
                        return userLevel;
                    }
                }
                userLevel = userLevelList.get(userLevelList.size() - 1).getName();
            }
        }
        return userLevel;
    }

    /**
     * 获取用户真实等级
     *
     * @param userId
     * @return
     */

    @Override
    public String getUserRealLevel(Long userId) {

        User curUser = userMapper.selectById(userId);

        String userLevel = "";

        List<UserLevel> userLevelList = userLevelMapper.selectListOrderByASC();

        //防止没有数据
        if (userLevelList.size() < 2)
            userLevel = "V1";
        else {
            Float a = userLevelList.get(0).getUpgradeVolume();
            Float b = curUser.getTransactionVolume();
            if (a > b) {
                userLevel = "V1";
            }
            for (int i = 1, size = userLevelList.size(); i < size; i++) {
                if (userLevelList.get(i).getUpgradeVolume() > curUser.getTransactionVolume()) {
                    userLevel = userLevelList.get(i - 1).getName();
                    return userLevel;
                }
            }
            userLevel = userLevelList.get(userLevelList.size() - 1).getName();
        }

        return userLevel;
    }

    /**
     * @param curUser
     * @return 当前用户等级，对应的分润比例，万分之多少
     */
    @Override
    public Float getUserLevelServiceCharge(User curUser) {

        //查询V1
        Float profitSplit = userLevelMapper.selectListOrderByASC().get(0).getProfitSplit();
        Float userLevelServiceCharge = 0f;

        // 判断是否到期
        if (curUser.getExpireDate() != null && !curUser.getExpireDate().before(new Date()) && !curUser.getStartDate().after(new Date())) {
            //查询用户手动级别
            userLevelServiceCharge = userLevelMapper.selectByName(curUser.getManualLevel()).getProfitSplit();
        } else {
            List<UserLevel> userLevelList = userLevelMapper.selectListOrderByASC();

            //防止没有数据
            if (userLevelList.size() == 0) {
                userLevelServiceCharge = profitSplit;
            } else {
                if (userLevelList.get(0).getUpgradeVolume() > curUser.getTransactionVolume()) {
                    userLevelServiceCharge = profitSplit;
                }
                for (int i = 1, size = userLevelList.size(); i < size; i++) {
                    if (userLevelList.get(i).getUpgradeVolume() >= curUser.getTransactionVolume()) {
                        userLevelServiceCharge = userLevelList.get(i - 1).getProfitSplit();
                        return userLevelServiceCharge;
                    }
                }
                userLevelServiceCharge = userLevelList.get(userLevelList.size() - 1).getProfitSplit();
            }
        }
        return userLevelServiceCharge;
    }

    /**
     * 根据流水通知，进行分润，根据等级与级差进行分润
     * A、一级代理商 L9 7.0(万分之)。
     * B、二级代理商 L8 6.5(万分之)。
     * C、三级代理商 L7 6.0(万分之)。
     * 逻辑如果是C的用户，消费一万元，给三级代理商6元：
     * C所属的直属上级代理商B，等级大于C，就给C的分润钱-B的分润钱，应该给B分万分之零点五（6.5-6.0）分0.5元。
     * B所属的直属上级代理商A，等级大于B，就给B的分润钱-A的分润钱，应该给A分万分之零点五（7.0-6.5）分0.5元、
     * 如果上级代理比自己等级相同，或者低就停止分钱，最高分到总代理商，也就是自己这里，魏总。
     * 这里分润的钱不能大于：一万元分7元（最高级对应的分润比例）。
     *
     * @param ctoZF
     * @return
     */
    @Override
    public void shareBenefit(CommercialTenantOrderZF ctoZF) {
        User user = sysPosTerminalMapper.selectByMachineNoAndClazzToUser(ctoZF.getTermSn(), ctoZF.getTermModel());

        List<UserLevel> userLevels = userLevelMapper.selectListOrderByDESC();

        //要分润的金额，单位：分
        float shareBenefitMoney = ctoZF.getAmount().floatValue(); // 获取交易额并转换为float

        // 最多分润的钱
        maxShareBenefit = (userLevels.get(0).getProfitSplit() / 10000f) * shareBenefitMoney;

        //进行递归分润
        grantShareBenefit(user, shareBenefitMoney, 0, 0);
    }

    /**
     * @param curUser                  当前的的用户
     * @param money                    要分润的钱
     * @param frontUserProfitSplit     上个用户的分润率
     * @param alreadyShareBenefitMoney 已经分润的钱，不能大于maxShareBenefit
     */
    public void grantShareBenefit(User curUser, float money, float frontUserProfitSplit, float alreadyShareBenefitMoney) {

        System.out.println("当前用户" + curUser.getUserName() + "  " + getUserLevelServiceCharge(curUser));
        if (getUserLevelServiceCharge(curUser) <= frontUserProfitSplit) {
            return;
        }

        //当前代理商实际的分润钱
        float myselfServiceCharge = (getUserLevelServiceCharge(curUser) - frontUserProfitSplit) / 10000f;

        //当前用户的分润比例
        frontUserProfitSplit = getUserLevelServiceCharge(curUser);

        //当前代理商应该分多少钱
        float income = money / 100 * (myselfServiceCharge);
        alreadyShareBenefitMoney += income;
        if (alreadyShareBenefitMoney <= maxShareBenefit) {
            curUser.setProfitSharingReward(curUser.getProfitSharingReward() + income);
            userMapper.updateById(curUser);
        }

        String parentUserName = userGradationService.getDirectParent(curUser.getUserName());
        if (parentUserName == null) {
            return;
        }

        //如果上级代理商的等级小于等于自己就不进行继续分润了
        User parentUser = userMapper.selectByUserNameToUser(parentUserName);
        if (getUserLevelServiceCharge(parentUser) <= getUserLevelServiceCharge(curUser)) {
            return;
        }
        grantShareBenefit(parentUser, money, frontUserProfitSplit, alreadyShareBenefitMoney);
    }
}
