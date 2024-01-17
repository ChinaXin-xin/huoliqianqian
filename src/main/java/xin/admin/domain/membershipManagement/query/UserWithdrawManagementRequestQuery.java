package xin.admin.domain.membershipManagement.query;

import lombok.Data;
import xin.admin.domain.membershipManagement.UserWithdrawManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UserWithdrawManagementRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private UserWithdrawManagement userWithdrawManagement;
    private List<UserWithdrawManagement> resultList; //返回的查询结果
    private Date startTime;      //查询的通用时间
    private Date endTime;        //查询结束时间

    public List<UserWithdrawManagement> getResultList() {
        if (startTime == null || endTime == null || this.resultList == null) {
            return resultList;
        }
        int num = 0;
        List<UserWithdrawManagement> newUserList = new ArrayList<>();
        for (UserWithdrawManagement u : resultList) {
            Date userDate = u.getWithdrawTime();
            if (userDate != null && !userDate.before(startTime) && !userDate.after(endTime)) {
                newUserList.add(u);
                num++;
            }
        }
        this.count = num;
        return newUserList;
    }
}
