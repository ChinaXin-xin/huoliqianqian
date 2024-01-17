package xin.admin.mapper.earningsManagenment;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xin.admin.domain.earningsManagenment.SysActivationReward;

@Mapper
public interface SysActivationRewardMapper extends BaseMapper<SysActivationReward> {
    IPage<SysActivationReward> selectPageWithUser(Page<SysActivationReward> page,
                                                  @Param("ew") Wrapper<SysActivationReward> queryWrapper);
}

