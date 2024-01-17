package xin.h5.service.myInformaation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.membershipManagement.UserAuthenticationInfoMapper;
import xin.admin.mapper.membershipManagement.UserLevelMapper;
import xin.admin.service.userInfomation.UserService;
import xin.common.domain.User;
import xin.h5.domain.myInformaation.PersonalInformation;
import xin.h5.service.myInformaation.MyInformationService;

import java.util.Calendar;
import java.util.Date;

@Service
public class MyInformationServiceImpl implements MyInformationService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserAuthenticationInfoMapper userAuthenticationInfoMapper;

    @Autowired
    UserLevelMapper userLevelMapper;

    @Autowired
    UserService userService;

    @Override
    public ResponseResult<PersonalInformation> getInfo() {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        PersonalInformation personalInformation = new PersonalInformation();

        personalInformation.setNickname(curUser.getNickname());
        personalInformation.setIcon(curUser.getIcon());
        personalInformation.setMyInvitationCode(curUser.getMyInvitationCode());
        personalInformation.setSuperiorPhone(userMapper.selectByInvitationCode(curUser.getInvitationCode()).getUserName());
        personalInformation.setAuthentication(userAuthenticationInfoMapper.isAuthentication(curUser.getId()));
        UserAuthenticationInfo userAuthenticationInfo = userAuthenticationInfoMapper.selectByUid(curUser.getId());

        // 如果没有提交过认证
        if (userAuthenticationInfo == null) {
            personalInformation.setAuthenticationStatus(-1);
        } else {
            personalInformation.setAuthenticationStatus(userAuthenticationInfo.getStatus());
        }


        personalInformation.setLevel(userService.getUserLevel(curUser));
        personalInformation.setMyPoints(curUser.getMyPoints());
        if (curUser.getMyPoints() == 0)
            personalInformation.setSignInDay(0);
        else
            personalInformation.setSignInDay((int) (curUser.getMyPoints() / PersonalInformation.everyDaySignPoints));
        return new ResponseResult<>(200, "查询成功！", personalInformation);
    }

    //判断日期是否是今天
    public static boolean isToday(Date date) {

        //如果用户没有签到过
        if (date == null)
            return false;

        // 获取当前日期的Calendar对象
        Calendar today = Calendar.getInstance();
        // 获取传入日期的Calendar对象
        Calendar specifiedDate = Calendar.getInstance();
        specifiedDate.setTime(date);

        // 比较年、月、日
        return (today.get(Calendar.YEAR) == specifiedDate.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == specifiedDate.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == specifiedDate.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ResponseResult signIn() {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        if (isToday(curUser.getUpdateTime())) {
            return new ResponseResult(400, "已签到");
        }
        curUser.setUpdateTime(new Date());
        curUser.setMyPoints(curUser.getMyPoints() + PersonalInformation.everyDaySignPoints);
        userMapper.updateById(curUser);
        return new ResponseResult(200, "签到成功！");
    }

    @Override
    public ResponseResult<User> getMyselfBonus() {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        curUser.setProfitSharingReward(curUser.getProfitSharingReward() / 100);
        curUser.setShareBenefit(curUser.getShareBenefit() / 100);
        curUser.setTransactionVolume(curUser.getTransactionVolume() / 100);
        return new ResponseResult(200, "查询成功！", curUser);
    }
}
