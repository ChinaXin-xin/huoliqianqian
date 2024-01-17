package xin.admin.controller.membershipManagement;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.query.UserTransactionsRequestQuery;
import xin.admin.service.membershipManagement.UserTransactionsService;

@RestController
@RequestMapping("/admin/userTransactions")
public class UserTransactionsController {

    @Autowired
    private UserTransactionsService userTransactionsService;

    @PostMapping("/list")
    public ResponseResult<UserTransactionsRequestQuery> list(@RequestBody UserTransactionsRequestQuery userTransactionsRequestQuery){
        return userTransactionsService.list(userTransactionsRequestQuery);
    }
}
