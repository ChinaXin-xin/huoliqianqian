package xin.admin.service.earningsManagenment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.SysTeamReward;
import xin.admin.domain.earningsManagenment.query.SysTeamRewardRequestQuery;
import xin.admin.mapper.earningsManagenment.SysTeamRewardMapper;
import xin.admin.service.earningsManagenment.SysTeamRewardService;

@Service
public class SysTeamRewardServiceImpl implements SysTeamRewardService {
    @Autowired
    SysTeamRewardMapper sysTeamRewardMapper;

    @Override
    public ResponseResult<SysTeamRewardRequestQuery> list(SysTeamRewardRequestQuery query) {
        QueryWrapper<SysTeamReward> wrapper = new QueryWrapper<>();
        SysTeamReward obj = query.getSysTeamReward();

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

        Page<SysTeamReward> page = new Page<>(query.getPageNumber(), query.getQuantity());
        IPage<SysTeamReward> result = sysTeamRewardMapper.selectPageWithUser(page, wrapper);

        query.setResultList(result.getRecords());
        query.setCount((int) result.getTotal());

        return new ResponseResult<>(200, "查询成功！", query);
    }
}
