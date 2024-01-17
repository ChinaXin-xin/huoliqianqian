package xin.admin.mapper.membershipManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xin.admin.domain.membershipManagement.UserScoreDetail;

import java.util.List;

@Mapper
public interface UserScoreDetailMapper extends BaseMapper<UserScoreDetail> {
    public List<UserScoreDetail> queryUserScoreDetailMsgList(
            @Param("userScoreDetail") UserScoreDetail userScoreDetail,
            @Param("offset") Integer offset,
            @Param("quantity") Integer quantity,
            @Param("userScoreDetailId") Long userScoreDetailId);

    public Integer queryUserScoreDetailMsgListCount(
            @Param("userScoreDetail") UserScoreDetail userScoreDetail,
            @Param("offset") Integer offset,
            @Param("quantity") Integer quantity,
            @Param("userScoreDetailId") Long userScoreDetailId);
}

