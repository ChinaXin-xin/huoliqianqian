package xin.admin.mapper.fundsFlowManagement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.admin.domain.fundsFlowManagement.CommercialTenantOrderZF;

import java.math.BigDecimal;
import java.util.Date;

@Mapper
public interface CommercialTenantOrderZFMapper extends BaseMapper<CommercialTenantOrderZF> {
    @Select("SELECT SUM(amount) FROM (\n" +
            "    SELECT rrn, amount FROM commercial_tenant_order_zf \n" +
            "    WHERE cardNo IS NOT NULL \n" +
            "    AND (sysRespCode = '00' OR sysRespCode = '10' OR sysRespCode = '11' OR sysRespCode IN ('A2', 'A4', 'A5', 'A6'))\n" +
            "    GROUP BY rrn\n" +
            ") AS grouped;\n")
    BigDecimal sumAmount();

    @Select("SELECT COUNT(*) FROM commercial_tenant_order_zf WHERE rrn=#{rrn}")
    int selectByRrnIsExist(@Param("rrn") String rrn);

    @Select("SELECT COUNT(*) FROM commercial_tenant_order_zf WHERE rrn = #{curRnn}")
    boolean existsByRrn(String curRnn);

    /**
     * 查询指定sn，一个月的交易额
     *
     * @param sn
     * @return
     */
    @Select("SELECT SUM(amount) FROM (\n" +
            "    SELECT rrn, amount FROM commercial_tenant_order_zf \n" +
            "    WHERE (sysRespCode = '00' OR sysRespCode = '10' OR sysRespCode = '11' OR sysRespCode IN ('A2', 'A4', 'A5', 'A6'))\n" +
            "    AND termSn LIKE CONCAT('%', #{sn}, '%')" +
            "    AND termModel=#{termModel} \n" +
            "    AND STR_TO_DATE(createtime, '%Y-%m-%d') >= DATE_FORMAT(CURRENT_DATE ,'%Y-%m-01')\n" +
            "    GROUP BY rrn\n" +
            ") AS grouped;\n")
    BigDecimal selectBySnThisMonthMoney(@Param("sn") String sn, @Param("termModel") String termModel);

    /**
     * 查询指定sn，历史的交易额
     *
     * @param sn
     * @return
     */
    @Select("SELECT SUM(amount) FROM (\n" +
            "    SELECT rrn, amount FROM commercial_tenant_order_zf \n" +
            "    WHERE (sysRespCode = '00' OR sysRespCode = '10' OR sysRespCode = '11' OR sysRespCode IN ('A2', 'A4', 'A5', 'A6'))\n" +
            "    AND termSn LIKE CONCAT('%', #{sn}, '%')" +
            "    AND termModel=#{termModel} \n" +
            "    GROUP BY rrn\n" +
            ") AS grouped;\n")
    BigDecimal selectBySnHistoryMoney(@Param("sn") String sn, @Param("termModel") String termModel);

    /**
     * 查询指定sn，一个月交易了多少次
     *
     * @param sn
     * @return
     */
    @Select("SELECT COUNT(*) FROM (\n" +
            "    SELECT rrn, amount FROM commercial_tenant_order_zf \n" +
            "    WHERE (sysRespCode = '00' OR sysRespCode = '10' OR sysRespCode = '11' OR sysRespCode IN ('A2', 'A4', 'A5', 'A6'))\n" +
            "    AND termSn LIKE CONCAT('%', #{sn}, '%')" +
            "    AND termModel=#{termModel} \n" +
            "    AND STR_TO_DATE(createtime, '%Y-%m-%d') >= DATE_FORMAT(CURRENT_DATE ,'%Y-%m-01')\n" +
            "    GROUP BY rrn\n" +
            ") AS grouped;\n")
    Integer selectBySnThisMonthNum(@Param("sn") String sn, @Param("termModel") String termModel);

    @Select("SELECT SUM(amount) FROM (\n" +
            "    SELECT rrn, amount FROM commercial_tenant_order_zf \n" +
            "    WHERE (sysRespCode = '00' OR sysRespCode = '10' OR sysRespCode = '11' OR sysRespCode IN ('A2', 'A4', 'A5', 'A6'))\n" +
            "    AND termSn LIKE CONCAT('%', #{sn}, '%')" +
            "    AND termModel=#{termModel} \n" +
            "    GROUP BY rrn\n" +
            ") AS grouped;\n")
    BigDecimal selectBySnMoney(@Param("sn") String sn, @Param("termModel") String termModel);

    @Select("SELECT SUM(amount) FROM (\n" +
            "    SELECT rrn, amount FROM commercial_tenant_order_zf \n" +
            "    WHERE (sysRespCode = '00' OR sysRespCode = '10' OR sysRespCode = '11' OR sysRespCode IN ('A2', 'A4', 'A5', 'A6'))\n" +
            "    AND termSn LIKE CONCAT('%', #{sn}, '%')" +
            "    AND termModel=#{termModel} \n" +
            "    AND DATE(createtime) = DATE(#{today}) \n" +
            "    GROUP BY rrn\n" +
            ") AS grouped;")
    BigDecimal selectBySnTodayMoney(@Param("sn") String sn, @Param("termModel") String termModel, @Param("today") Date today);

    @Select("SELECT SUM(amount) FROM (\n" +
            "    SELECT rrn, amount FROM commercial_tenant_order_zf \n" +
            "    WHERE (sysRespCode = '00' OR sysRespCode = '10' OR sysRespCode = '11' OR sysRespCode IN ('A2', 'A4', 'A5', 'A6'))\n" +
            "    AND termSn LIKE CONCAT('%', #{sn}, '%')" +
            "    AND termModel=#{termModel} \n" +
            "    AND YEAR(createtime) = YEAR(#{specifiedDate}) AND MONTH(createtime) = MONTH(#{specifiedDate}) \n" +
            "    GROUP BY rrn\n" +
            ") AS grouped;")
    BigDecimal selectBySnSpecifiedYearCurrentMonth(@Param("sn") String sn, @Param("termModel") String termModel, @Param("specifiedDate") Date specifiedDate);
}
