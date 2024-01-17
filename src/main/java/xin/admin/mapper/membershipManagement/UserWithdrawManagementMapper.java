package xin.admin.mapper.membershipManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.membershipManagement.UserWithdrawManagement;

import java.util.List;

@Mapper
public interface UserWithdrawManagementMapper extends BaseMapper<UserWithdrawManagement> {
    List<UserWithdrawManagement> selectAll();

    List<UserWithdrawManagement> selectPage(@Param("userWithdrawManagement") UserWithdrawManagement userWithdrawManagement,
                                            @Param("offset") Integer offset,
                                            @Param("quantity") Integer quantity);

    Integer selectPageCount(@Param("userWithdrawManagement") UserWithdrawManagement userWithdrawManagement,
                            @Param("offset") Integer offset,
                            @Param("quantity") Integer quantity);

    UserWithdrawManagement mySelectById(@Param("id") Integer id);

    @Select("SELECT\n" +
            "    *\n" +
            "FROM\n" +
            "    user_withdraw_management uwm\n" +
            "    JOIN user_authentication_info uai ON uwm.user_authentication_info_id = uai.uid\n" +
            "WHERE\n" +
            "    1 = 1\n" +
            "    AND uwm.id = #{uid} ")
    UserWithdrawManagement mySelectByUid(@Param("uid") Integer uid);

    /**
     * 查询绑定的实名认证信息，这个也是sys_user的id
     * @param userAuthenticationInfoId
     * @return
     */
    @Select("select * from user_withdraw_management where user_authentication_info_id=#{userAuthenticationInfoId}  ")
    UserWithdrawManagement selectByUserAuthenticationInfoId(@Param("userAuthenticationInfoId") Integer userAuthenticationInfoId);
}
