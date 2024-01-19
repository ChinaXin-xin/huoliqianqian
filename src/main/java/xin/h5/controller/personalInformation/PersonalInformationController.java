package xin.h5.controller.personalInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.h5.domain.personalInformation.AccumulatedTotal;
import xin.h5.domain.personalInformation.DealPerformance;
import xin.h5.domain.personalInformation.query.DealPerformanceRequestQuery;
import xin.h5.service.invitation.PersonalInformationService;

@RestController
@RequestMapping("/h5/personalInformation/deal")
public class PersonalInformationController {

    @Autowired
    PersonalInformationService personalInformationService;

    /**
     * 获取自己与一下所有人累计的交易总和，等其它信息
     *
     * @return
     */
    @PostMapping("/getMyselfDealMoney")
    public ResponseResult<AccumulatedTotal> getMyselfDealMoney() {
        return personalInformationService.getMyselfDealMoneyAndOther();
    }

    /**
     * 获取自己当前月的交易总和
     *
     * @return
     */
    @PostMapping("/getMyselfThisMonthDealMoney")
    public ResponseResult<DealPerformance> getThisMonthDealMoneyAndOther() {
        return personalInformationService.getThisMonthDealMoneyAndOther();
    }

    /**
     * 获取直属下级累计的交易总和等信息，本月的
     *
     * @return
     */
    @PostMapping("/getOurThisMonthDealMoney")
    public ResponseResult<DealPerformance> getOurThisMonthDealMoney() {
        return personalInformationService.getOurThisMonthDealMoneyAndOther();
    }

    /**
     * 查询指定日业绩中的个人业绩中的日维度，查询自己和直属下级的交易信息
     *
     * @param query
     * @return
     */
    @PostMapping("/selectPageTodayMsg")
    ResponseResult<DealPerformanceRequestQuery> selectPageMyselfTodayMsg(@RequestBody DealPerformanceRequestQuery query) {
        return personalInformationService.selectPageMyselfTodayMsg(query);
    }

    /**
     * 个人业绩中的月维度，查询自己和直属下级的交易信息
     *
     * @param query
     * @return
     */
    @PostMapping("/selectPageMyselfThisMonthMsg")
    ResponseResult<DealPerformanceRequestQuery> selectPageMyselfThisMonthMsg(@RequestBody DealPerformanceRequestQuery query) {
        return personalInformationService.selectPageMyselfThisMonthMsg(query);
    }

    /**
     * 查询指定月业绩中的团队业绩中的日维度，查询自己所有的下级，不包括自己
     *
     * @param query
     * @return
     */
    @PostMapping("/selectPageOurTodayMsg")
    ResponseResult<DealPerformanceRequestQuery> selectPageOurTodayMsg(@RequestBody DealPerformanceRequestQuery query) {
        return personalInformationService.selectPageOurTodayMsg(query);
    }

    /**
     * 查询指定月业绩中的团队业绩中的越维度，查询自己所有的下级，不包括自己
     *
     * @param query
     * @return
     */
    @PostMapping("/selectPageOurMonthMsg")
    ResponseResult<DealPerformanceRequestQuery> selectPageOurMonthMsg(@RequestBody DealPerformanceRequestQuery query) {
        return personalInformationService.selectPageOurMonthMsg(query);
    }

    /**
     * 查询指定月业绩中的累计业绩中的日维度，所有人的
     *
     * @param query
     * @return
     */
    @PostMapping("/selectPageAllTodayMsg")
    ResponseResult<DealPerformanceRequestQuery> selectPageAllTodayMsg(@RequestBody DealPerformanceRequestQuery query) {
        return personalInformationService.selectPageAllTodayMsg(query);
    }

    /**
     * 查询指定月业绩中的全部的业绩中的月维度，所有人的
     *
     * @param query
     * @return
     */
    @PostMapping("/selectPageAllMonthMsg")
    ResponseResult<DealPerformanceRequestQuery> selectPageAllMonthMsg(@RequestBody DealPerformanceRequestQuery query) {
        return personalInformationService.selectPageAllMonthMsg(query);
    }

    /**
     * 获取当日数据
     *
     * @param
     * @return
     */
    @PostMapping("/getDayData")
    public ResponseResult<DealPerformanceRequestQuery> getDayData(@RequestBody DealPerformanceRequestQuery query) {
        if (query.getType().equals("selectPageTodayMsg")) {
            //个人业绩中的日维度
            //return selectPageMyselfTodayMsg(query);
            return selectPageAllTodayMsg(query);
        } else if (query.getType().equals("selectPageOurTodayMsg")) {
            //团队业绩中的日维度
            return selectPageOurTodayMsg(query);
        } else if (query.getType().equals("selectPageAllTodayMsg")) {
            //累计的日维度
            return selectPageAllTodayMsg(query);
        }
        return new ResponseResult<>(400, "请求类型错误");
    }

    @PostMapping("/getMonthData")
    public ResponseResult<DealPerformanceRequestQuery> getMonthData(@RequestBody DealPerformanceRequestQuery query) {
        if (query.getType().equals("selectPageMyselfThisMonthMsg")) {
            //人业绩中的月维度
            //return selectPageMyselfThisMonthMsg(query);
            return selectPageAllMonthMsg(query);
        } else if (query.getType().equals("selectPageOurMonthMsg")) {
            //团队业绩中的月维度
            return selectPageOurMonthMsg(query);
        } else if (query.getType().equals("selectPageAllMonthMsg")) {
            //累计的的月维度
            return selectPageAllMonthMsg(query);
        }
        return new ResponseResult<>(400, "请求类型错误");
    }
}
