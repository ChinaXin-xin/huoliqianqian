package xin.admin.domain.earningsManagenment.query;

import lombok.Data;
import xin.admin.domain.earningsManagenment.SysActivityReward;

import java.util.Date;
import java.util.List;

@Data
public class SysActivityRewardRequestQuery {
    private Integer pageNumber;
    private Integer quantity;
    private Integer count;
    private SysActivityReward sysActivityReward;
    private List<SysActivityReward> resultList;
    private Date startTime;
    private Date endTime;

    public Integer getCount() {
        if (count == null)
            return 0;
        return count;
    }
}
