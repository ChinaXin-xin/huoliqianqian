package xin.admin.mapper.fundsFlowManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.fundsFlowManagement.BindMachineInformZF;

@Mapper
public interface BindMachineInformZFMapper extends BaseMapper<BindMachineInformZF> {
    @Select("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM bind_machine_inform_zf WHERE term_sn = #{curTermSn} and term_model=#{termModel}")
    boolean existsWithTermSn(@Param("curTermSn") String curTermSn, @Param("termModel") String termModel);
}
