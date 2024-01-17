package xin.admin.controller.earningsManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.query.UserScoreRequestQuery;
import xin.admin.service.earningsManagenment.UserScoreService;


/**
 * 收益管理，我的积分接口
 */
@RestController
@RequestMapping("/admin/earningsManagement/myselfScore")
public class UserScoreController {

    @Autowired
    UserScoreService userScoreService;

    @PostMapping("/list")
    public ResponseResult<UserScoreRequestQuery> list(@RequestBody UserScoreRequestQuery query){
        return userScoreService.list(query);
    }
}
