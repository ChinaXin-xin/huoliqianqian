package xin.admin.mapper.fundsFlowManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.common.domain.User;
import xin.h5.domain.myselfMachine.MachineSnBelongUser;
import xin.h5.domain.myselfMachine.MerchantDetails;
import xin.h5.domain.myselfMachine.MyselfMachineNum;

import java.util.Date;
import java.util.List;

@Mapper
public interface SysPosTerminalMapper extends BaseMapper<SysPosTerminal> {
    Page<SysPosTerminal> selectSysPosTerminal(Page<SysPosTerminal> page, @Param("query") SysPosTerminal sysPosTerminal);

    Integer selectSysPosTerminalCount(@Param("query") SysPosTerminal sysPosTerminal);

    Page<SysPosTerminal> selectSysPosTerminalTransferManagement(Page<SysPosTerminal> page, @Param("query") SysPosTerminal sysPosTerminal);

    Integer selectSysPosTerminalCountTransferManagement(@Param("query") SysPosTerminal sysPosTerminal);

    int deleteByMachineNo(@Param("machineNo") String machineNo, @Param("clazz") String clazz);

    int selectByMachineNo(@Param("machineNo") String machineNo);

    /**
     * @param machineNo
     * @return
     */
    User selectByMachineNoMsg(@Param("machineNo") String machineNo, @Param("clazz") String clazz);


    /**
     * 根据sn查询手机号
     *
     * @param machineNo
     * @return
     */
    User selectByMachineNoAndClazzToUser(@Param("machineNo") String machineNo, @Param("clazz") String clazz);

    /**
     * 根据sn查询机器的信息
     *
     * @param machineNo
     * @return
     */
    SysPosTerminal selectByMachineNoToMachineMsg(@Param("machineNo") String machineNo, @Param("clazz") String clazz);

    /**
     * 根据sn查询机器的信息和型号
     *
     * @param machineNo
     * @return
     */
    SysPosTerminal selectByMachineNoToMachineAndClazzMsg(@Param("machineNo") String machineNo, @Param("clazz") String clazz);

    /**
     * 写错地方了。。。。
     *
     * @param phone
     * @return
     */
    @Select("SELECT * from sys_user where phone=#{phone} ")
    User selectByPhone(@Param("phone") String phone);


    /**
     * 查询自己有多少没激活的pos机
     *
     * @param uid
     * @return
     */
    @Select("SELECT COUNT(*) from sys_pos_terminal where uid=#{uid} and type!=3")
    Integer selectByUidNoActivateNum(@Param("uid") Long uid);

    /**
     * 把sn吗对应的机器划入用户，把uid与用户的id绑定
     * 划分的时候必须是从仓库，也就是uid=100的账号划分出去，已经划分给别人的，也就是uid不为100的，不能划分
     *
     * @param id
     * @param machineNo
     * @return
     */
    @Update("UPDATE sys_pos_terminal SET uid=#{id} where machine_no=#{machineNo} and clazz=#{clazz}  and uid=100")
    int transfer(@Param("id") Integer id, @Param("machineNo") String machineNo, @Param("clazz") String clazz);

    /**
     * 把sn吗对应的机器划入用户，把uid与用户的id绑定
     * 划分的时候必须是从仓库，也就是uid=100的账号划分出去，已经划分给别人的，也就是uid不为100的，不能划分
     *
     * @param id
     * @param machineNo
     * @return
     */
    @Update("UPDATE sys_pos_terminal SET uid=#{id}, authentication_num=1 where machine_no=#{machineNo} and clazz= #{clazz} and uid=#{uid}")
    int userTransfer(@Param("id") Integer id, @Param("machineNo") String machineNo, @Param("clazz") String clazz, @Param("uid") Long uid);

    /**
     * 判断机器是否是从管理员划拨出去的
     *
     * @param id
     * @param machineNo
     * @return
     */
    @Select("select COUNT(*) from sys_pos_terminal  where machine_no=#{machineNo} and uid=100 and clazz=#{clazz} ")
    int transferIsAccordWith(@Param("id") Integer id, @Param("machineNo") String machineNo, @Param("clazz") String clazz);


    /**
     * 划拨到自己，100为管理员，也就是自己
     *
     * @param
     * @return
     */
    @Update("UPDATE sys_pos_terminal SET uid=100 where machine_no=#{machineNo}")
    int transferAdmin(@Param("machineNo") String machineNo);

    @Update("UPDATE sys_pos_terminal SET update_time=#{date} where machine_no=#{machineNo}  and clazz=#{clazz}")
    int updateDate(@Param("machineNo") String machineNo, @Param("date") Date date, @Param("clazz") String clazz);

    @Update("UPDATE sys_pos_terminal SET machine_no=#{machineNo}  where update_time=#{date} and clazz=#{clazz}")
    int updateDateAndClazz(@Param("machineNo") String machineNo, @Param("date") Date date, @Param("clazz") String clazz);

    /**
     * 把绑定通知中的sn和型号与库存中的做比对，有的话就设置为3，已激活
     *
     * @param sn
     * @param posType
     * @return
     */
    @Update("UPDATE sys_pos_terminal SET type='3' , update_time=#{curDate} where machine_no=#{sn} and clazz=#{posType}")
    int bindMachine(@Param("sn") String sn, @Param("posType") String posType, @Param("curDate") Date curDate);

    /**
     * 查询哪个是自己已经激活的机器
     *
     * @param uid
     * @return
     */
    @Select("select * from sys_pos_terminal where uid=#{uid} and type='3' ")
    List<SysPosTerminal> selectByUidIsActivate(@Param("uid") Long uid);


    /**
     * 查询哪个是自己已经激活的机器，而且必须是当前月份一号开始的
     *
     * @param uid
     * @return
     */
    @Select("SELECT * FROM sys_pos_terminal " + "WHERE uid=#{uid} " + "AND type='3' " + "AND update_time >= DATE_FORMAT(NOW(),'%Y-%m-01')")
    List<SysPosTerminal> selectByUidThisMonth(@Param("uid") Long uid);

    /**
     * 查询自己指定日激活的机器
     *
     * @param uid
     * @return
     */
    @Select("SELECT * FROM sys_pos_terminal WHERE uid = #{uid} AND type = '3' AND DATE(update_time) = DATE(#{time})")
    List<SysPosTerminal> selectByUidToday(@Param("uid") Long uid, @Param("time") Date time);

    /**
     * 查询指定用户在指定月份激活的机器
     *
     * @param uid  用户ID
     * @param time 指定的月份
     * @return 在指定月份内激活的机器列表
     */
    @Select("SELECT * FROM sys_pos_terminal WHERE uid = #{uid} AND type = '3' " + "AND YEAR(update_time) = YEAR(#{time}) " + "AND MONTH(update_time) = MONTH(#{time})")
    List<SysPosTerminal> selectByUidMonth(@Param("uid") Long uid, @Param("time") Date time);


    /**
     * 用户机具品牌与对应激活的数量
     * 查询
     *
     * @param uid
     * @return
     */
    @Select("SELECT clazz as type, COUNT(*) AS number\n" + "FROM sys_pos_terminal\n" + "WHERE uid = #{uid} \n" + "GROUP BY clazz;\n")
    List<MyselfMachineNum> queryMyselfMachineTypeAndNumber(@Param("uid") Long uid);


    /**
     * 查询自己没有激活的机器
     *
     * @param page
     * @param sysPosTerminal
     * @return
     */
    IPage<MachineSnBelongUser> selectByUidToMyselfTypeMachineAlreadyActivate(Page<MachineSnBelongUser> page, @Param("uid") Long uid, @Param("sysPosTerminal") SysPosTerminal sysPosTerminal);

    /**
     * 查询指定类型自己已经激活的SN
     *
     * @param sysPosTerminal
     * @return
     */
    List<MachineSnBelongUser> selectByUidToMyselfTypeMachineNotActivate(@Param("uid") Long uid, @Param("sysPosTerminal") SysPosTerminal sysPosTerminal);

    /**
     * 更新商户编号与真实姓名，商户名称
     *
     * @param merchantName 商户名
     * @param who          名字
     * @param merchantId   商户号
     * @param sn           编号
     * @param idCard       身份证号
     * @return
     */
    @Update("UPDATE sys_pos_terminal SET merchant_id = #{merchantId}, merchant_name = #{merchantName}, who = #{who}, id_card = #{idCard}  WHERE machine_no = #{sn}")
    int updatePosTerminalMerchantMsg(@Param("merchantName") String merchantName, @Param("who") String who, @Param("merchantId") String merchantId, @Param("sn") String sn, @Param("idCard") String idCard);

    /**
     * 根据商户号，查询商户详情
     *
     * @return
     */
    @Select("SELECT \n" + "    u.icon AS icon,\n" + "    t.who AS who,\n" + "    t.clazz AS clazz,            \n" + "    t.create_time AS create_time,\n" + "    t.update_time AS update_time,\n" + "    t.machine_no AS machine_no  \n" + "FROM \n" + "    sys_pos_terminal t\n" + "JOIN \n" + "    sys_user u ON t.uid = u.id\n" + "WHERE merchant_id = #{merchantId}\n")
    MerchantDetails queryByMerchantIdDetails(@Param("merchantId") String merchantId);

    /**
     * 根据商户号，查询pos机信息
     *
     * @param merchantId
     * @return
     */
    @Select("SELECT * \n" + "FROM \n" + "    sys_pos_terminal \n" + "WHERE merchant_id = #{merchantId}\n")
    SysPosTerminal queryByMerchantIdToSysPosTerminal(@Param("merchantId") String merchantId);

    @Select("select count(*) from sys_pos_terminal where machine_no=#{machineNo} and clazz=#{clazz} and authentication_num=0")
    Boolean selectByMachineNoAndClazzAndActivateMsg(@Param("machineNo") String machineNo, @Param("clazz") String clazz);

    /**
     * 用户机具品牌与对应激活的数量
     * 查询
     *
     * @param uid
     * @return
     */
    //@MapKey("clazz")
/*    @Select("SELECT clazz, COUNT(*) AS number \n" + "FROM sys_pos_terminal\n" + "WHERE uid = #{uid} \n" + "GROUP BY clazz;\n")
    List<Map<String, Long>> queryMachineTypeAndNumber(@Param("uid") Long uid);*/
}
