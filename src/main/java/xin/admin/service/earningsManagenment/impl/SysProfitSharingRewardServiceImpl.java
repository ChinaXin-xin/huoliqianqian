package xin.admin.service.earningsManagenment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.SysProfitSharingReward;
import xin.admin.domain.earningsManagenment.query.SysProfitSharingRewardRequestQuery;
import xin.admin.mapper.earningsManagenment.SysProfitSharingRewardMapper;
import xin.admin.service.earningsManagenment.SysProfitSharingRewardService;

@Service
public class SysProfitSharingRewardServiceImpl implements SysProfitSharingRewardService {
    @Autowired
    SysProfitSharingRewardMapper sysProfitSharingRewardMapper;

    @Override
    public ResponseResult<SysProfitSharingRewardRequestQuery> list(SysProfitSharingRewardRequestQuery query) {
        QueryWrapper<SysProfitSharingReward> wrapper = new QueryWrapper<>();
        SysProfitSharingReward obj = query.getSysProfitSharingReward();

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

        Page<SysProfitSharingReward> page = new Page<>(query.getPageNumber(), query.getQuantity());
        IPage<SysProfitSharingReward> result = sysProfitSharingRewardMapper.selectPageWithUser(page, wrapper);

        query.setResultList(result.getRecords());
        query.setCount((int) result.getTotal());

        return new ResponseResult<>(200, "查询成功！", query);
    }

}
