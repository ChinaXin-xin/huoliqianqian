package xin.admin.domain.membershipManagement.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xin.admin.domain.membershipManagement.UserTransactions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionsRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private UserTransactions userTransactions;
    private List<UserTransactions> resultList; //返回的查询结果
    private Date startTime;      //查询的通用时间
    private Date endTime;        //查询结束时间

    public List<UserTransactions> getResultList() {
        if (startTime == null || endTime == null || this.resultList == null) {
            return resultList;
        }
        int num = 0;
        List<UserTransactions> newUserList = new ArrayList<>();
        for (UserTransactions u : resultList) {
            Date userDate = u.getCreatedAt();
            if (userDate != null && !userDate.before(startTime) && !userDate.after(endTime)) {
                newUserList.add(u);
                num++;
            }
        }
        this.count = num;
        return newUserList;
    }
}
