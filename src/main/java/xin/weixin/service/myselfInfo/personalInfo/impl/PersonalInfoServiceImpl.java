package xin.weixin.service.myselfInfo.personalInfo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.mapper.UserMapper;
import xin.common.domain.User;
import xin.weixin.service.myselfInfo.personalInfo.PersonalInfoService;

@Service
public class PersonalInfoServiceImpl extends ServiceImpl<UserMapper, User> implements PersonalInfoService {

    @Override
    public ResponseResult<User> getMyselfInfo() {

        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("my_invitation_code", curUser.getInvitationCode());

        User superior = this.getOne(qw);
        curUser.setSuperiorPhone(superior.getUserName());
        return new ResponseResult<>(200, "查询成功！", curUser);
    }

    @Override
    public ResponseResult updateInfo(User user) {
        if (this.updateById(user)){
            return new ResponseResult<>(200, "更新成功！");
        }
        return new ResponseResult<>(200, "更新失败！");
    }
}
