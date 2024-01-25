package xin.admin.mapper.fundsFlowManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.fundsFlowManagement.PosFeeRate;

@Mapper
public interface PosFeeRateMapper extends BaseMapper<PosFeeRate> {

    @Select("select * from pos_fee_rate where pos_id=#{posId} and sys_fee_rate_id=#{sysFeeRateId}")
    PosFeeRate selectByPosId(@Param("posId") Integer posId, @Param("sysFeeRateId") Integer sysFeeRateId);
}
