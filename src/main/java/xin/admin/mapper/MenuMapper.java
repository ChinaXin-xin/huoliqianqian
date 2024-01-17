package xin.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xin.admin.domain.Menu;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 查询用户的权限
     * @param userid
     * @return
     */
    List<String> selectPermsByUserId(Long userid);
}
