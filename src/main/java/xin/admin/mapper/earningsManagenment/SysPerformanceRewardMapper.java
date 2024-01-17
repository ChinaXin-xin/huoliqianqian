package xin.admin.mapper.earningsManagenment;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xin.admin.domain.earningsManagenment.SysPerformanceReward;

@Mapper
public interface SysPerformanceRewardMapper extends BaseMapper<SysPerformanceReward> {
    IPage<SysPerformanceReward> selectPageWithUser(Page<SysPerformanceReward> page,
                                                   @Param("ew") Wrapper<SysPerformanceReward> queryWrapper);
}

