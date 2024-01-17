package xin.admin.domain.earningsManagenment.query;

import lombok.Data;
import xin.admin.domain.earningsManagenment.SysProfitSharingReward;

import java.util.Date;
import java.util.List;

@Data
public class SysProfitSharingRewardRequestQuery {
    private Integer pageNumber;
    private Integer quantity;
    private Integer count;
    private SysProfitSharingReward sysProfitSharingReward;
    private List<SysProfitSharingReward> resultList;
    private Date startTime;
    private Date endTime;

    public Integer getCount() {
        if (count == null)
            return 0;
        return count;
    }
}
