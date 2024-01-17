package xin.admin.service.adminHomePage;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.adminHomePage.UserMsgRequestQuery;
import xin.admin.domain.adminHomePage.UserPortionMsg;
import xin.common.domain.User;

/**
 * 用户管理->修改弹窗中的信息
 */
public interface AdminHomePageService {
    ResponseResult<UserMsgRequestQuery> query(UserMsgRequestQuery requestQuery);

    ResponseResult alterUserMsg(User user);

    ResponseResult<User> selectUserMsgById(Long id);

    ResponseResult<UserPortionMsg> queryUserPortionMsg(Long id);

    ResponseResult alterUserPortionMsg(UserPortionMsg userPortionMsg);
}
