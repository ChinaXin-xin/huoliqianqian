package xin.h5.mapper.wallet;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.h5.domain.wallet.SysAward;

@Mapper
public interface SysAwardMapper extends BaseMapper<SysAward> {

    @Select("select * from sys_award where uid = #{uid} ")
    SysAward queryUserAwardStatus(@Param("uid") Long uid);
}
