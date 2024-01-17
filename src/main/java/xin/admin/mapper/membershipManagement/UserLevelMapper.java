package xin.admin.mapper.membershipManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.UserLevel;

import java.util.List;

@Mapper
public interface UserLevelMapper extends BaseMapper<UserLevel> {
    public List<UserLevel> queryUserLevelMsgList(
            @Param("pageNumber") Integer pageNumber,
            @Param("quantity") Integer quantity);

    /**
     * 按交易量升序排序
     *
     * @return
     */
    @Select("SELECT * FROM sys_user_level ORDER BY upgrade_volume ASC ")
    List<UserLevel> selectListOrderByASC();

    /**
     * 按交易量升序排序
     *
     * @return
     */
    @Select("SELECT * FROM sys_user_level ORDER BY upgrade_volume DESC ")
    List<UserLevel> selectListOrderByDESC();


    /**
     * 根据级别名称查询分润比例
     *
     * @return
     */
    @Select("SELECT * FROM sys_user_level where name=#{name} ")
    UserLevel selectByName(@Param("name") String name);
}
