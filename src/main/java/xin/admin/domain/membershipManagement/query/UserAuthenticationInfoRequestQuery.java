package xin.admin.domain.membershipManagement.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticationInfoRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private UserAuthenticationInfo userAuthenticationInfo;       //总共多少条
    private List<UserAuthenticationInfo> resultList; //返回的查询结果
    private Date startTime;      //查询的通用时间
    private Date endTime;        //查询结束时间

    public List<UserAuthenticationInfo> getResultList() {
        if (startTime == null || endTime == null || this.resultList == null) {
            return resultList;
        }
        int num = 0;
        List<UserAuthenticationInfo> newUserList = new ArrayList<>();
        for (UserAuthenticationInfo u : resultList) {
            Date userDate = u.getApplicationDate();
            if (userDate != null && !userDate.before(startTime) && !userDate.after(endTime)) {
                newUserList.add(u);
                num++;
            }
        }
        this.count = num;
        return newUserList;
    }
}
