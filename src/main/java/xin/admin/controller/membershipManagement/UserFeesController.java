package xin.admin.controller.membershipManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserFees;
import xin.admin.service.membershipManagement.UserFeesService;

@RestController
@RequestMapping("/admin/UserFees")
public class UserFeesController {

    @Autowired
    private UserFeesService userFeesService;

    @PostMapping("/select")
    public ResponseResult<UserFees> select() {
        return userFeesService.select();
    }

    @PostMapping("/alter")
    public ResponseResult<UserFees> select(@RequestBody UserFees userFees) {
        return userFeesService.alter(userFees);
    }
}
