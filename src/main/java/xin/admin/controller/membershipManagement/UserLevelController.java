package xin.admin.controller.membershipManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.UserLevel;
import xin.admin.domain.membershipManagement.query.UserLevelRequestQuery;
import xin.admin.service.membershipManagement.UserLevelService;

@RestController
@RequestMapping("/admin/membershipManagement")
public class UserLevelController {

    @Autowired
    private UserLevelService userLevelService;

    @PostMapping("/add")
    public ResponseResult addUserLevel(@RequestBody UserLevel userLevel) {
        userLevelService.addUserLevel(userLevel);
        return new ResponseResult(200, "信息添加成功");
    }

    @PostMapping("/delete/{id}")
    public ResponseResult deleteUserLevel(@PathVariable Long id) {
        userLevelService.deleteUserLevelById(id);
        return new ResponseResult<>(200, "信息删除成功");
    }

    @PostMapping("/list")
    public ResponseResult<UserLevelRequestQuery> getAllUserLevels(@RequestBody UserLevelRequestQuery userLevelRequestQuery) {
        UserLevelRequestQuery resultQuery;
        if (userLevelRequestQuery.getQueryUserLevelId() != null) {
            resultQuery = userLevelService.getAllUserLevels(userLevelRequestQuery);
            if (resultQuery.getCount() == -1)
                return new ResponseResult<>(401, "信息获取失败，未能找到id为：" + userLevelRequestQuery.getQueryUserLevelId() + " 的用户等级信息", resultQuery);
        } else {
            resultQuery = userLevelService.getAllUserLevels(userLevelRequestQuery);
        }
        return new ResponseResult<>(200, "信息获取成功", resultQuery);
    }

    @PostMapping("/update")
    public ResponseResult updateUserLevel(@RequestBody UserLevel userLevel) {
        userLevelService.updateUserLevel(userLevel);
        return new ResponseResult(200, "信息更新成功");
    }
}
