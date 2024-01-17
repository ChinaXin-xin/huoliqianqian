package xin.admin.mapper.membershipManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;

import java.util.List;

@Mapper
public interface UserAuthenticationInfoMapper extends BaseMapper<UserAuthenticationInfo> {
    List<UserAuthenticationInfo> list(@Param("userAuthenticationInfo") UserAuthenticationInfo userAuthenticationInfo,
                                      @Param("offset") Integer offset,
                                      @Param("quantity") Integer quantity);

    Integer listCount(@Param("userAuthenticationInfo") UserAuthenticationInfo userAuthenticationInfo,
                      @Param("offset") Integer offset,
                      @Param("quantity") Integer quantity);

    /**
     * 查询是否实名
     *
     * @param uid
     * @return
     */
    @Select("select count(*) from user_authentication_info where uid=#{uid} and status=1")
    Boolean isAuthentication(@Param("uid") Long uid);

    @Select("select * from user_authentication_info where uid=#{uid}")
    UserAuthenticationInfo selectByUid(@Param("uid") Long uid);

    /**
     * 判断审核是否被拒绝
     * @param uid
     * @return
     */
    @Select("select count(*) > 0 from user_authentication_info where uid=#{uid} and status = 2")
    Boolean selectByUidIsPass(@Param("uid") Long uid);
}
