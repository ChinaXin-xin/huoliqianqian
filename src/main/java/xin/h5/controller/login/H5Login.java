package xin.h5.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.mapper.UserMapper;
import xin.admin.service.login.LoginService;
import xin.common.domain.User;

@RestController
@RequestMapping("/h5/account")
public class H5Login {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        return loginService.login(user, false);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                user.setUserName(user.getPhone());
            }
        }

        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            if (user.getUserName() != null && !user.getUserName().isEmpty()) {
                user.setPhone(user.getUserName());
            }
        }
        return loginService.register(user);
    }

    @PostMapping("/changePassword")
    public ResponseResult changePassword(@RequestBody User user) {
        return loginService.changePassword(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }
}