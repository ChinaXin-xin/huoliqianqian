package xin.admin.domain.earningsManagenment.query;

import lombok.Data;
import xin.admin.domain.earningsManagenment.SysReferralReward;

import java.util.Date;
import java.util.List;

@Data
public class SysReferralRewardRequestQuery {
    private Integer pageNumber;
    private Integer quantity;
    private Integer count;
    private SysReferralReward sysReferralReward;
    private List<SysReferralReward> resultList;
    private Date startTime;
    private Date endTime;

    public Integer getCount() {
        if (count == null)
            return 0;
        return count;
    }
}
