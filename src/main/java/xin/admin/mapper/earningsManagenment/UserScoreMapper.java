package xin.admin.mapper.earningsManagenment;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xin.admin.domain.earningsManagenment.UserScore;
import xin.admin.domain.earningsManagenment.query.UserScoreRequestQuery;

@Mapper
public interface UserScoreMapper extends BaseMapper<UserScore> {
    Page<UserScore> selectUserScoreWithUserInfo(Page<UserScore> page, @Param("userScore") UserScore userScore,
                                                @Param("query")UserScoreRequestQuery query);

    Integer selectUserScoreWithUserInfoCount(@Param("userScore") UserScore userScore,
                                             @Param("query")UserScoreRequestQuery query);
}
