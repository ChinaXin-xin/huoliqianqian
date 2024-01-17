package xin.admin.mapper.contentManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.contentManagement.SysBanner;

import java.util.List;

@Mapper
public interface SysBannerMapper extends BaseMapper<SysBanner> {
    List<SysBanner> selectPaging(@Param("sysBanner") SysBanner sysBanner,
                                 @Param("offset") Integer offset,
                                 @Param("quantity") Integer quantity);

    Integer selectPagingCount(@Param("sysBanner") SysBanner sysBanner,
                              @Param("offset") Integer offset,
                              @Param("quantity") Integer quantity);

    @Select("select * from sys_banner where status = 1")
    List<SysBanner> userList();

}
