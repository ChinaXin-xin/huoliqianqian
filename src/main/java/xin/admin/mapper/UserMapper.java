package xin.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.adminHomePage.UserPortionMsg;
import xin.common.domain.User;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 根据用户名判断用户是否是管理员
    @Select("SELECT * FROM sys_user WHERE user_name=#{userName} AND password=#{password} ")
    public User queryByUserPassword(@Param("userName") String userName, @Param("password") String password);


    // 根据用户名判断用户是否是管理员
    @Select("SELECT CASE WHEN count(*) > 0 THEN true ELSE false END FROM sys_user WHERE user_name=#{userName} AND (user_type = '0' or user_type = '2') ")
    public boolean queryIsAdmin(@Param("userName") String userName);

    //根据user里的信息去查询
    public Integer queryCommonUserMsgCount(@Param("user") User user);

    //根据user里的信息去查询，并且加上分页
    public List<User> queryCommonUserMsgList(@Param("user") User user, @Param("pageNumber") Integer pageNumber, @Param("quantity") Integer quantity);

    /**
     * 更改密碼
     *
     * @return
     */
    int changePassword(@Param("user") User user);

    @Select("SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM sys_user WHERE user_name = #{account} OR phone = #{account}")
    boolean checkUserNameOrPhoneExists(@Param("account") String account);

    @Select("SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM sys_user WHERE (user_name = #{account} OR phone = #{account}) and user_type='0'")
    boolean checkIsAdmin(@Param("account") String account);

    /**
     * 判断这个这个邀请码是否存在，防止用户注册给用户创建唯一邀请码的时候与其它用户的邀请码重复
     *
     * @param invitationCode 要验证的邀请码
     * @return
     */
    @Select("select COUNT(*) from sys_user where my_invitation_code = #{invitationCode}")
    Integer isInvitationCodeExist(@Param("invitationCode") String invitationCode);


    @Select("select * from sys_user where user_name = #{userName}")
    User selectByUserNameToUser(@Param("userName") String userName);

    /**
     * 根据邀请码，查询邀请人
     *
     * @param invitationCode
     * @return
     */
    @Select("select * from sys_user where my_invitation_code=#{invitationCode}")
    User selectByInvitationCode(@Param("invitationCode") String invitationCode);

    /**
     * 查询自己邀请了多少人
     */
    @Select("select * from sys_user where invitation_code=#{myInvitationCode}")
    List<User> selectByMyInvitationUserList(@Param("myInvitationCode") String myInvitationCode);

    /**
     * 查询指定用户，本月邀请新用户注册信息
     *
     * @param myInvitationCode
     * @return
     */
    @Select("SELECT * FROM sys_user " + "WHERE invitation_code=#{myInvitationCode} " + "AND create_time >= DATE_FORMAT(NOW(),'%Y-%m-01')")
    List<User> selectByMyInvitationThisMonthUserList(@Param("myInvitationCode") String myInvitationCode);

    /**
     * 查询指定用户，指定日，邀请新用户注册信息
     *
     * @param myInvitationCode
     * @return
     */
    @Select("SELECT * FROM sys_user " + "WHERE invitation_code = #{myInvitationCode} " + "AND DATE(create_time) = DATE(#{time})")
    List<User> selectByMyInvitationTodayUserList(@Param("myInvitationCode") String myInvitationCode, @Param("time") Date time);

    /**
     * 查询指定用户，在指定月份邀请的新用户注册信息
     *
     * @param myInvitationCode 用户的邀请码
     * @param time             指定的月份
     * @return 符合条件的用户列表
     */
    @Select("SELECT * FROM sys_user " + "WHERE invitation_code = #{myInvitationCode} " + "AND YEAR(create_time) = YEAR(#{time}) " + "AND MONTH(create_time) = MONTH(#{time})")
    List<User> selectByMyInvitationMonthUserList(@Param("myInvitationCode") String myInvitationCode, @Param("time") Date time);

    /**
     * 用户管理->修改弹窗中的信息
     */
    @Select("SELECT nickname, user_name,profit_sharing_reward, my_invitation_code, create_time, activation_award, activity_award, achievement_award, team_award, share_benefit, my_points FROM sys_user WHERE id=#{id}")
    UserPortionMsg selectByIdToUserPortionMsg(@Param("id") Long id);
}
