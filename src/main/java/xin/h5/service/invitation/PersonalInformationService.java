package xin.h5.service.invitation;

import org.springframework.web.bind.annotation.RequestBody;
import xin.admin.domain.ResponseResult;
import xin.h5.domain.personalInformation.AccumulatedTotal;
import xin.h5.domain.personalInformation.DealPerformance;
import xin.h5.domain.personalInformation.query.DealPerformanceRequestQuery;

import java.util.Date;

public interface PersonalInformationService {
    //查询本月累计的交易额与其它信息
    ResponseResult<DealPerformance> getThisMonthDealMoneyAndOther();

    //查询自己以及下属所有的人的激活机器数量
    Integer selectOurPosActivateNum();

    //查询自己以及下属所有的人数
    Integer selectOurPersonNum();

    //查询示例累计的交易额与其它信息
    ResponseResult<AccumulatedTotal> getMyselfDealMoneyAndOther();

    //根据用户名查询一个人的pos机，激活的数量
    Integer selectByUserNameActivatePosNum(String userName);

    //根据用户名查询一个人的pos机，激活的数量，本月的
    Integer selectByUserNameActivatePosNumThisMonth(String userName);

    //查询本月累计的交易额与其它信息
    ResponseResult<DealPerformance> getOurThisMonthDealMoneyAndOther();

    //根据用户名查询新增商户（pos机）当天的
    Integer selectNewlyIncreasedPosNumToday(String userName, Date queryDate);


    //根据用户名查询新增伙伴、当天的
    Integer selectNewlyIncreasedPersonNumToday(String userName, Date queryDate);

    //查询本月业绩中的个人业绩，中的日维度，查询自己和直属下级的交易信息
    ResponseResult<DealPerformanceRequestQuery> selectPageMyselfTodayMsg(DealPerformanceRequestQuery query);

    //查询个人本月的记录
    ResponseResult<DealPerformanceRequestQuery> selectPageMyselfThisMonthMsg(@RequestBody DealPerformanceRequestQuery query);

    /**
     * 根据用户名查询新增伙伴、当月的
     *
     * @param userName
     * @return
     */
    public Integer selectNewlyIncreasedPersonNumMoney(String userName, Date queryDate);

    /**
     * 根据用户名查询新增商户（pos机）指定月的
     *
     * @param userName
     * @return
     */
    public Integer selectNewlyIncreasedPosNumMoneyMonth(String userName, Date queryDate);

    /**
     * 查询指定月业绩中的团队业绩中的日维度，查询自己所有的下级，不包括自己
     *
     * @param query
     * @return
     */
    ResponseResult<DealPerformanceRequestQuery> selectPageOurTodayMsg(@RequestBody DealPerformanceRequestQuery query);

    /**
     * 查询指定月业绩中的团队业绩中的越维度，查询自己所有的下级，不包括自己
     *
     * @param query
     * @return
     */
    ResponseResult<DealPerformanceRequestQuery> selectPageOurMonthMsg(@RequestBody DealPerformanceRequestQuery query);

    /**
     * 查询指定月业绩中的累计业绩中的日维度，查询自己所有的下级，不包括自己
     *
     * @param query
     * @return
     */
    ResponseResult<DealPerformanceRequestQuery> selectPageAllTodayMsg(@RequestBody DealPerformanceRequestQuery query);

    /**
     * 查询指定月业绩中的全部的业绩中的月维度，
     *
     * @param query
     * @return
     */
    ResponseResult<DealPerformanceRequestQuery> selectPageAllMonthMsg(@RequestBody DealPerformanceRequestQuery query);
}
