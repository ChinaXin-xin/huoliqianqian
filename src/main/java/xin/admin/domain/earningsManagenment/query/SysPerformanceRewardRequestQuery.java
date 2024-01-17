package xin.admin.domain.earningsManagenment.query;

import lombok.Data;
import xin.admin.domain.earningsManagenment.SysPerformanceReward;

import java.util.Date;
import java.util.List;

@Data
public class SysPerformanceRewardRequestQuery {
    private Integer pageNumber;
    private Integer quantity;
    private Integer count;
    private SysPerformanceReward sysPerformanceReward;
    private List<SysPerformanceReward> resultList;
    private Date startTime;
    private Date endTime;

    public Integer getCount() {
        if (count == null)
            return 0;
        return count;
    }
}
