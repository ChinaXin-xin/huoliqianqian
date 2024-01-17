package xin.admin.domain.earningsManagenment.query;

import lombok.Data;
import xin.admin.domain.earningsManagenment.SysTeamReward;

import java.util.Date;
import java.util.List;

@Data
public class SysTeamRewardRequestQuery {
    private Integer pageNumber;
    private Integer quantity;
    private Integer count;
    private SysTeamReward sysTeamReward;
    private List<SysTeamReward> resultList;
    private Date startTime;
    private Date endTime;

    public Integer getCount() {
        if (count == null)
            return 0;
        return count;
    }
}
