package xin.admin.controller.membershipManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;
import xin.admin.domain.membershipManagement.query.UserAuthenticationInfoRequestQuery;
import xin.admin.service.membershipManagement.UserAuthenticationInfoService;

@RestController
@RequestMapping("/admin/userAuthentication")
public class UserAuthenticationInfoController {

    @Autowired
    private UserAuthenticationInfoService userAuthenticationInfoService;

    @PostMapping("/list")
    public ResponseResult<UserAuthenticationInfoRequestQuery> list(@RequestBody UserAuthenticationInfoRequestQuery query) {
        return userAuthenticationInfoService.list(query);
    }

    @PostMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        return userAuthenticationInfoService.delete(id);
    }

    @PostMapping("/alter")
    public ResponseResult alter(@RequestBody UserAuthenticationInfo userAuthenticationInfo) {
        return userAuthenticationInfoService.alter(userAuthenticationInfo);
    }
}
