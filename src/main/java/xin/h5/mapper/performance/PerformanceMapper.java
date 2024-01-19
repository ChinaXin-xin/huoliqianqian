package xin.h5.mapper.performance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xin.h5.domain.performance.Performance;

import java.math.BigDecimal;
import java.util.Date;

@Mapper
public interface PerformanceMapper extends BaseMapper<Performance> {

    // 查询pos机所有的交易额，如果没有某天的记录就返回null，记得进行判空一下
    @Select("select SUM(today_money) from sys_performance " +
            "where term_sn=#{termSn} and term_model=#{termModel} ")
    BigDecimal selectBySnAndModel(
            @Param("termSn") String termSn, @Param("termModel") String termModel);

    // 查询pos机某天的交易额，如果没有某天的记录就返回null，记得进行判空一下
    @Select("select today_money from sys_performance " +
            "where term_sn=#{termSn} and term_model=#{termModel} and DATE(create_time)=DATE(#{someDay}) ")
    BigDecimal selectBySnToSomeDayMoney(
            @Param("termSn") String termSn, @Param("termModel") String termModel, @Param("someDay") Date someDay);


    @Select("SELECT SUM(today_money) FROM sys_performance " +
            "WHERE term_sn=#{termSn} AND term_model=#{termModel} " +
            "AND YEAR(create_time) = YEAR(#{someDay}) " +
            "AND MONTH(create_time) = MONTH(#{someDay})")
    BigDecimal selectBySnToSomeMonthMoney(
            @Param("termSn") String termSn, @Param("termModel") String termModel, @Param("someDay") Date someMonth);


    // 查询pos机某天的交易额，如果没有某天的记录就返回null，记得进行判空一下
    @Select("select * from sys_performance " +
            "where term_sn=#{termSn} and term_model=#{termModel} and DATE(create_time)=DATE(#{someDay}) ")
    Performance selectBySnToSomeDayPerformance(
            @Param("termSn") String termSn, @Param("termModel") String termModel, @Param("someDay") Date someDay);

    // 查询pos机某天到某天的交易额，如果没有某天的记录就返回null，记得进行判空一下
    @Select("select SUM(today_money) from sys_performance " +
            "where term_sn=#{termSn} and term_model=#{termModel} and DATE(create_time) BETWEEN DATE(#{startDate}) AND DATE(#{endDate})")
    BigDecimal selectBySnAndDateRange(
            @Param("termSn") String termSn, @Param("termModel") String termModel, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // 查询pos机某月到某月的交易额，如果没有某天的记录就返回null，记得进行判空一下
    @Select("select SUM(today_money) from sys_performance " +
            "where term_sn=#{termSn} and term_model=#{termModel} " +
            "and CONCAT(YEAR(create_time), '-', LPAD(MONTH(create_time), 2, '0')) >= CONCAT(YEAR(#{startDate}), '-', LPAD(MONTH(#{startDate}), 2, '0')) " +
            "and CONCAT(YEAR(create_time), '-', LPAD(MONTH(create_time), 2, '0')) <= CONCAT(YEAR(#{endDate}), '-', LPAD(MONTH(#{endDate}), 2, '0'))")
    BigDecimal selectBySnAndMonthRange(
            @Param("termSn") String termSn, @Param("termModel") String termModel, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 返回最新添加一条记录
     * @return
     */
    @Select("SELECT * FROM sys_performance ORDER BY id DESC LIMIT 1;")
    Performance selectByRecently();

}
