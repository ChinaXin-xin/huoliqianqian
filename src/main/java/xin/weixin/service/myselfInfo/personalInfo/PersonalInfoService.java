package xin.weixin.service.myselfInfo.personalInfo;

import com.baomidou.mybatisplus.extension.service.IService;
import xin.admin.domain.ResponseResult;
import xin.common.domain.User;

public interface PersonalInfoService extends IService<User> {
    ResponseResult<User> getMyselfInfo();

    ResponseResult updateInfo(User user);
}
