package xin.admin.service.earningsManagenment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.SysPerformanceReward;
import xin.admin.domain.earningsManagenment.query.SysPerformanceRewardRequestQuery;
import xin.admin.mapper.earningsManagenment.SysPerformanceRewardMapper;
import xin.admin.service.earningsManagenment.SysPerformanceRewardService;

@Service
public class SysPerformanceRewardServiceImpl implements SysPerformanceRewardService {
    @Autowired
    SysPerformanceRewardMapper sysPerformanceRewardMapper;

    @Override
    public ResponseResult<SysPerformanceRewardRequestQuery> list(SysPerformanceRewardRequestQuery query) {
        QueryWrapper<SysPerformanceReward> wrapper = new QueryWrapper<>();
        SysPerformanceReward obj = query.getSysPerformanceReward();

        if (obj != null) {
            if (StringUtils.isNotEmpty(obj.getName())) {
                wrapper.like("u.name", obj.getName());
            }
            if (StringUtils.isNotEmpty(obj.getPhone())) {
                wrapper.like("u.phone", obj.getPhone());
            }
        }

        if (query.getStartTime() != null) {
            wrapper.ge("reward_time", query.getStartTime());
        }
        if (query.getEndTime() != null) {
            wrapper.le("reward_time", query.getEndTime());
        }

        Page<SysPerformanceReward> page = new Page<>(query.getPageNumber(), query.getQuantity());
        IPage<SysPerformanceReward> result = sysPerformanceRewardMapper.selectPageWithUser(page, wrapper);

        query.setResultList(result.getRecords());
        query.setCount((int) result.getTotal());

        return new ResponseResult<>(200, "查询成功！", query);
    }

}
