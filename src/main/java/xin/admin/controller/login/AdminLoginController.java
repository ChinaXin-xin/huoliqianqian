package xin.admin.controller.login;

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
@RequestMapping("/admin")
public class AdminLoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    private UserMapper userMapper;


    @PostMapping("/login")
    public ResponseResult login(@RequestBody User admin) {
        if (userMapper.queryIsAdmin(admin.getUserName()))
            return loginService.login(admin, true);
        return new ResponseResult<>(401, "登录失败您不是管理员");
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        return loginService.register(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }
}
