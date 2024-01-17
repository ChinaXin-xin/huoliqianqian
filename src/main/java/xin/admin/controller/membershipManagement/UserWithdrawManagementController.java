package xin.admin.controller.membershipManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserWithdrawManagement;
import xin.admin.domain.membershipManagement.query.UserWithdrawManagementRequestQuery;
import xin.admin.service.membershipManagement.UserWithdrawManagementService;

import java.util.List;

@RestController
@RequestMapping("/admin/withdrawManagement")
public class UserWithdrawManagementController {

    @Autowired
    private UserWithdrawManagementService userWithdrawManagementService;

    @PostMapping("/listAll")
    public ResponseResult<List<UserWithdrawManagement>> selectAll() {
        return userWithdrawManagementService.selectAll();
    }

    @PostMapping("/list")
    public ResponseResult<UserWithdrawManagementRequestQuery> select(@RequestBody UserWithdrawManagementRequestQuery query) {
        return userWithdrawManagementService.select(query);
    }

    @PostMapping("/alter")
    public ResponseResult select(@RequestBody UserWithdrawManagement userWithdrawManagement) {
        return userWithdrawManagementService.alter(userWithdrawManagement);
    }
}
