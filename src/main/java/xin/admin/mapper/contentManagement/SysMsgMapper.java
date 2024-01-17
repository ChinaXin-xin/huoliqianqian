package xin.admin.mapper.contentManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.contentManagement.SysMsg;

import java.util.List;

@Mapper
public interface SysMsgMapper extends BaseMapper<SysMsg> {
    List<SysMsg> selectPaging(@Param("sysMsg") SysMsg sysMsg,
                              @Param("offset") Integer offset,
                              @Param("quantity") Integer quantity);

    Integer selectPagingCount(@Param("sysMsg") SysMsg sysMsg,
                              @Param("offset") Integer offset,
                              @Param("quantity") Integer quantity);

    @Select("select * from sys_msg where status = 1")
    List<SysMsg> userList();
}
