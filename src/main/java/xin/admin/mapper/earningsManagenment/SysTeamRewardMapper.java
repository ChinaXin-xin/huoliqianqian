package xin.admin.mapper.earningsManagenment;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xin.admin.domain.earningsManagenment.SysTeamReward;

@Mapper
public interface SysTeamRewardMapper extends BaseMapper<SysTeamReward> {
    IPage<SysTeamReward> selectPageWithUser(Page<SysTeamReward> page,
                                            @Param("ew") Wrapper<SysTeamReward> queryWrapper);
}

