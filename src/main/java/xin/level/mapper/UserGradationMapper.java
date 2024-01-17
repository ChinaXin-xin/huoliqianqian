package xin.level.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import xin.level.domain.UserGradation;

@Mapper
@Repository
public interface UserGradationMapper extends BaseMapper<UserGradation> {
    // 这里可以根据需要添加自定义的数据库操作方法
}
