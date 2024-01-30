package xin.admin.mapper.fundsFlowManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.fundsFlowManagement.SysFeeDeductionRecord;

@Mapper
public interface SysFeeDeductionRecordMapper extends BaseMapper<SysFeeDeductionRecord> {
    @Select("SELECT * FROM sys_fee_deduction_record WHERE `status`='成功' AND type='流量' AND pos=#{sn} ORDER BY id DESC LIMIT 1")
    SysFeeDeductionRecord queryRecentlyRecord(@Param("sn") String sn);

}
