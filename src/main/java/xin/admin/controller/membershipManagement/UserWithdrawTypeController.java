package xin.admin.controller.membershipManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserWithdrawType;
import xin.admin.service.membershipManagement.UserWithdrawTypeService;

import java.util.List;

@RestController
@RequestMapping("/admin/withdrawManagement/withdrawType")
public class UserWithdrawTypeController {
    @Autowired
    UserWithdrawTypeService userWithdrawTypeService;

    @PostMapping("/listAll")
    public ResponseResult<List<UserWithdrawType>> selectAll() {
        return userWithdrawTypeService.selectAll();
    }
}
