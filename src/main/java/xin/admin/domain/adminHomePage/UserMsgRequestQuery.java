package xin.admin.domain.adminHomePage;

import lombok.Data;
import xin.common.domain.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用于查询用户
 */
@Data
public class UserMsgRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private User user;           //关键字
    private Integer count;       //总共多少条
    private List<User> userList; //返回的查询结果
    private Date startTime; //开始时间
    private Date endTime;   //结束时间

    //判断是否在查询的两个日期中间
    public List<User> getUserList() {
        if (startTime == null || endTime == null || this.userList == null) {
            return userList;
        }

        List<User> newUserList = new ArrayList<>();
        for (User user : userList) {
            Date userDate = user.getExpireDate();
            if (userDate != null && !userDate.before(startTime) && !userDate.after(endTime)) {
                newUserList.add(user);
            }
        }
        return newUserList;
    }
}