package xin.admin.service.earningsManagenment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.SysActivationReward;
import xin.admin.domain.earningsManagenment.query.SysActivationRewardRequestQuery;
import xin.admin.mapper.earningsManagenment.SysActivationRewardMapper;
import xin.admin.service.earningsManagenment.SysActivationRewardService;

@Service
public class SysActivationRewardServiceImpl implements SysActivationRewardService {
    @Autowired
    SysActivationRewardMapper sysActivationRewardMapper;

    @Override
    public ResponseResult<SysActivationRewardRequestQuery> list(SysActivationRewardRequestQuery query) {
        QueryWrapper<SysActivationReward> wrapper = new QueryWrapper<>();
        SysActivationReward searchCriteria = query.getSysActivationReward();

        // 模糊查询条件
        if (searchCriteria != null) {
            if (StringUtils.isNotEmpty(searchCriteria.getName())) {
                wrapper.like("u.name", searchCriteria.getName());
            }
            if (StringUtils.isNotEmpty(searchCriteria.getPhone())) {
                wrapper.like("u.phone", searchCriteria.getPhone());
            }
        }

        // 时间范围过滤
        if (query.getStartTime() != null) {
            wrapper.ge("reward_time", query.getStartTime());
        }
        if (query.getEndTime() != null) {
            wrapper.le("reward_time", query.getEndTime());
        }

        // 分页查询
        Page<SysActivationReward> page = new Page<>(query.getPageNumber(), query.getQuantity());
        IPage<SysActivationReward> result = sysActivationRewardMapper.selectPageWithUser(page, wrapper);

        // 设置结果和总条数
        query.setResultList(result.getRecords());
        query.setCount((int) result.getTotal());

        return new ResponseResult<>(200, "查询成功！", query);
    }

}
