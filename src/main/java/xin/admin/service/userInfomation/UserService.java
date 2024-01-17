package xin.admin.service.userInfomation;

import xin.admin.domain.fundsFlowManagement.CommercialTenantOrderZF;
import xin.common.domain.User;

public interface UserService {

    //获取用户等级
    String getUserLevel(User curUser);

    //获取用户真实等级
    String getUserRealLevel(Long userId);

    //获取用户等级对应的费率
    Float getUserLevelServiceCharge(User curUser);

    //分润
    void shareBenefit(CommercialTenantOrderZF ctoZF);
}
