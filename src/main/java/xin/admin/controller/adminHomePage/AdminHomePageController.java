package xin.admin.controller.adminHomePage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.adminHomePage.UserMsgRequestQuery;
import xin.admin.domain.adminHomePage.UserPortionMsg;
import xin.admin.service.adminHomePage.AdminHomePageService;
import xin.common.domain.User;

@RestController
@RequestMapping("/admin/adminHomePage")
public class AdminHomePageController {

    public void test() {
        System.out.println("李义新");
        while (true);
    }

    @Autowired
    AdminHomePageService adminHomePageService;

    /**
     * 查询用户信息
     *
     * @param requestQuery
     * @return
     */
    @PostMapping("/query")
    ResponseResult<UserMsgRequestQuery> query(@RequestBody UserMsgRequestQuery requestQuery) {
        return adminHomePageService.query(requestQuery);
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @PostMapping("/alterUserMsg")
    ResponseResult alterUserMsg(@RequestBody User user) {
        return adminHomePageService.alterUserMsg(user);
    }

    /**
     * 根据id查询用户所有信息
     *
     * @param id 用户id
     * @return
     */
    @PostMapping("/selectUserMsgById/{id}")
    ResponseResult<User> selectUserMsgById(@PathVariable Long id) {
        return adminHomePageService.selectUserMsgById(id);
    }

    /**
     * 查询部分信息，昵称，账号，用户邀请码，新的密码 会员级别，邀请码，入网时间。sys_user
     * [姓名，手机号，银行卡账号，银行卡支行，是否实名认证]实名认证的。 user_authentication
     * 积分，激活奖，推荐奖，分润奖，活动奖，达标奖，团队奖。sys_user
     * 机具机器-数组[品牌名：数量]
     *
     * @param id 用户id
     * @return
     */
    @PostMapping("/queryUserPortionMsg/{id}")
    ResponseResult<UserPortionMsg> queryUserPortionMsg(@PathVariable Long id) {
        return adminHomePageService.queryUserPortionMsg(id);
    }

    @PostMapping("/alterUserPortionMsg")
    ResponseResult alterUserPortionMsg(@RequestBody UserPortionMsg userPortionMsg) {
        return adminHomePageService.alterUserPortionMsg(userPortionMsg);
    }
}
