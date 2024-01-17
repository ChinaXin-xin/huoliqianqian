package xin.admin.controller.membershipManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.membershipManagement.UserScoreDetail;
import xin.admin.domain.membershipManagement.query.UserScoreDetailRequestQuery;
import xin.admin.service.membershipManagement.UserScoreDetailService;

@RestController
@RequestMapping("/admin/scoreDetail")
public class UserScoreDetailController {
    @Autowired
    UserScoreDetailService userScoreDetailService;

    @PostMapping("/list")
    public ResponseResult<UserScoreDetailRequestQuery> selectUserScoreDetailPage(@RequestBody UserScoreDetailRequestQuery query) {
        return userScoreDetailService.selectUserScoreDetailPage(query);
    }

    @PostMapping("/add")
    public ResponseResult add(@RequestBody UserScoreDetail userScoreDetail) {
        return userScoreDetailService.add(userScoreDetail);
    }
}
