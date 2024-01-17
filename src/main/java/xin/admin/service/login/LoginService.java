package xin.admin.service.login;

import xin.admin.domain.ResponseResult;
import xin.common.domain.User;

public interface LoginService {
    ResponseResult login(User user,Boolean isAdmin);

    ResponseResult register(User user);

    ResponseResult changePassword(User user);

    public ResponseResult logout();
}
