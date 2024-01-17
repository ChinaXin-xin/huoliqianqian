package xin.admin.domain.earningsManagenment.query;

import lombok.Data;
import xin.admin.domain.earningsManagenment.SysActivationReward;

import java.util.Date;
import java.util.List;

@Data
public class SysActivationRewardRequestQuery {
    private Integer pageNumber;  // 页码
    private Integer quantity;    // 每页多少条
    private Integer count;       // 总共多少条
    private SysActivationReward sysActivationReward; // 用于搜索的对象
    private List<SysActivationReward> resultList; // 返回的查询结果
    private Date startTime; // 开始时间
    private Date endTime;   // 结束时间

    public Integer getCount() {
        if (count == null)
            return 0;
        return count;
    }
}
