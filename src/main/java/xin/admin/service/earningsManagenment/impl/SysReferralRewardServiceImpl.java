package xin.admin.service.earningsManagenment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.earningsManagenment.SysReferralReward;
import xin.admin.domain.earningsManagenment.query.SysReferralRewardRequestQuery;
import xin.admin.mapper.earningsManagenment.SysReferralRewardMapper;
import xin.admin.service.earningsManagenment.SysReferralRewardService;

@Service
public class SysReferralRewardServiceImpl implements SysReferralRewardService {
    @Autowired
    SysReferralRewardMapper sysReferralRewardMapper;

    @Override
    public ResponseResult<SysReferralRewardRequestQuery> list(SysReferralRewardRequestQuery query) {
        QueryWrapper<SysReferralReward> wrapper = new QueryWrapper<>();
        SysReferralReward obj = query.getSysReferralReward();

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

        Page<SysReferralReward> page = new Page<>(query.getPageNumber(), query.getQuantity());
        IPage<SysReferralReward> result = sysReferralRewardMapper.selectPageWithUser(page, wrapper);

        query.setResultList(result.getRecords());
        query.setCount((int) result.getTotal());

        return new ResponseResult<>(200, "查询成功！", query);
    }

}
