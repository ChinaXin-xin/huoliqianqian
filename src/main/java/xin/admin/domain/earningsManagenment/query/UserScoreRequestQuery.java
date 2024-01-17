package xin.admin.domain.earningsManagenment.query;

import lombok.Data;
import xin.admin.domain.earningsManagenment.UserScore;

import java.util.Date;
import java.util.List;

@Data
public class UserScoreRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private UserScore userScore;
    private List<UserScore> resultList; //返回的查询结果
    private Date startTime;
    private Date endTime;
    public Integer getCount() {
        if (count == null)
            return 0;
        return count;
    }
}
