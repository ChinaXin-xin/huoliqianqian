package xin.weixin.level.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import xin.weixin.level.domain.WxUserGradation;

@Mapper
@Repository
public interface WxUserGradationMapper extends BaseMapper<WxUserGradation> {
    // 这里可以根据需要添加自定义的数据库操作方法
}
