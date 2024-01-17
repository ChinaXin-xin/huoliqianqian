package xin.admin.service.earningsManagenment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.SysActivityReward;
import xin.admin.domain.earningsManagenment.query.SysActivityRewardRequestQuery;
import xin.admin.mapper.earningsManagenment.SysActivityRewardMapper;
import xin.admin.service.earningsManagenment.SysActivityRewardService;

@Service
public class SysActivityRewardServiceImpl implements SysActivityRewardService {
    @Autowired
    SysActivityRewardMapper sysActivityRewardMapper;

    @Override
    public ResponseResult<SysActivityRewardRequestQuery> list(SysActivityRewardRequestQuery query) {
        QueryWrapper<SysActivityReward> wrapper = new QueryWrapper<>();
        SysActivityReward obj = query.getSysActivityReward();

        // 模糊查询条件
        if (obj != null) {
            if (StringUtils.isNotEmpty(obj.getName())) {
                wrapper.like("u.name", obj.getName());
            }
            if (StringUtils.isNotEmpty(obj.getPhone())) {
                wrapper.like("u.phone", obj.getPhone());
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
        Page<SysActivityReward> page = new Page<>(query.getPageNumber(), query.getQuantity());
        IPage<SysActivityReward> result = sysActivityRewardMapper.selectPageWithUser(page, wrapper);

        // 设置结果和总条数
        query.setResultList(result.getRecords());
        query.setCount((int) result.getTotal());

        return new ResponseResult<>(200, "查询成功！", query);
    }
}
