package xin.admin.controller.membershipManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;
import xin.admin.service.membershipManagement.UserAuthenticationInfoService;

@RestController
@RequestMapping("/h5/userAuthentication")
public class UserAuthenticationInfoControllerH5 {

    @Autowired
    private UserAuthenticationInfoService userAuthenticationInfoService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody UserAuthenticationInfo userAuthenticationInfo) {
        return userAuthenticationInfoService.add(userAuthenticationInfo);
    }
}
