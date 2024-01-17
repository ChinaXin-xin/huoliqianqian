package xin.admin.domain.membershipManagement.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xin.admin.domain.membershipManagement.UserScoreDetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserScoreDetailRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private List<UserScoreDetail> resultList; //返回的查询结果
    private UserScoreDetail userScoreDetail;
    private Long userScoreDetailId;    //要查询的id
    private Date startTime;      //查询的通用时间
    private Date endTime;        //查询结束时间

    public List<UserScoreDetail> getResultList() {
        if (startTime == null || endTime == null || this.resultList == null) {
            return resultList;
        }
        int num = 0;
        List<UserScoreDetail> newUserList = new ArrayList<>();
        for (UserScoreDetail user : resultList) {
            Date userDate = user.getCreationTime();
            if (userDate != null && !userDate.before(startTime) && !userDate.after(endTime)) {
                newUserList.add(user);
                num++;
            }
        }
        this.count = num;
        return newUserList;
    }
}
