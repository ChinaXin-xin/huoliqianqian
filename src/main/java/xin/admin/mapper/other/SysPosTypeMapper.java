package xin.admin.mapper.other;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.other.SysPosType;

@Mapper
public interface SysPosTypeMapper extends BaseMapper<SysPosType> {

    //判断类型是否存在
    @Select("select count(*) from sys_pos_type where type=#{type}")
    Boolean isTypeExist(@Param("type") String type);
}
