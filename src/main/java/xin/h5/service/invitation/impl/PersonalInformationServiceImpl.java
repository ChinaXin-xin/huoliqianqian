package xin.h5.service.invitation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.fundsFlowManagement.CommercialTenantOrderZFMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.common.domain.User;
import xin.h5.domain.personalInformation.AccumulatedTotal;
import xin.h5.domain.personalInformation.DealPerformance;
import xin.h5.domain.personalInformation.query.DealPerformanceRequestQuery;
import xin.h5.service.invitation.PersonalInformationService;
import xin.level.service.UserGradationService;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PersonalInformationServiceImpl implements PersonalInformationService {

    @Autowired
    CommercialTenantOrderZFMapper commercialTenantOrderZFMapper;

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    UserGradationService userGradationService;

    @Autowired
    UserMapper userMapper;

    /**
     * 获取自己的当月交易额等信息
     *
     * @return
     */
    @Override
    public ResponseResult<DealPerformance> getThisMonthDealMoneyAndOther() {
        DealPerformance result = new DealPerformance();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        String myInvitationCode = loginUser.getUser().getMyInvitationCode();
        List<User> myInvitationUserList = userMapper.selectByMyInvitationUserList(myInvitationCode);
        List<User> thisMonthNewPartnerList = userMapper.selectByMyInvitationThisMonthUserList(myInvitationCode);
        result.setAccumulativeTotalPartner(myInvitationUserList.size()); //用户历史邀请人数
        result.setNewPartner(thisMonthNewPartnerList.size()); //当前月历史邀请人数

        //如果自己没有Pos机，所以就没有交易额，本月交易额直接是0
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(userid);
        if (sysPosTerminalList == null || sysPosTerminalList.isEmpty()) {
            result.setMoney(BigDecimal.ZERO);
        } else {
            BigDecimal money = BigDecimal.ZERO;
            for (SysPosTerminal sysPosTerminal : sysPosTerminalList) {
                if (sysPosTerminal == null) {
                    // 可以考虑记录日志，但不返回，继续处理其他项
                    continue;
                }
                BigDecimal thisMonthMoney = commercialTenantOrderZFMapper.selectBySnThisMonthMoney(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz());
                if (thisMonthMoney != null) {
                    money = money.add(thisMonthMoney);
                }
            }
            result.setMoney(money);  //本月自己累计的交易金额
        }

        //获取自己与直属下级的userName
        //List<String> directDescendants = userGradationService.getDirectDescendants(loginUser.getUser().getUserName());
        List<String> directDescendants = new ArrayList<>();
        directDescendants.add(loginUser.getUser().getUserName());

        //下面是自己历史激活数量-------------------------------
        int historyActivateNum = 0;
        for (String un : directDescendants) {
            historyActivateNum += selectByUserNameActivatePosNum(un);
        }
        result.setAccumulativeTotalActivatePos(historyActivateNum);

        //下面是自己历史激活数量-------------------------------
        int thisMonthActivateNum = 0;
        for (String un : directDescendants) {
            thisMonthActivateNum += selectByUserNameActivatePosNumThisMonth(un);
        }
        result.setNewActivatePos(thisMonthActivateNum);

        return new ResponseResult<>(200, "查询成功！", result);
    }

    /**
     * 自己和下级一共激活的pos机
     *
     * @return
     */
    @Override
    public Integer selectOurPosActivateNum() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        int count = 0;
        User user = userMapper.selectById(userid);
        //查询用户以及
        List<String> userAndDescendants = userGradationService.getUserAndDescendants(user.getUserName());

        List<User> userList = new ArrayList<>();
        for (String userName : userAndDescendants) {
            User subUser = userMapper.selectByUserNameToUser(userName);
            if (subUser != null)
                userList.add(subUser);
        }

        for (User un : userList) {
            List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(un.getId());
            if (sysPosTerminalList == null || sysPosTerminalList.isEmpty()) {
                System.out.println("警告，" + un.getUserName() + " 未绑定pos机");
            } else {
                count += sysPosTerminalList.size();
            }
        }
        return count;
    }


    /**
     * 获取所有下级一共多少人
     *
     * @return
     */
    @Override
    public Integer selectOurPersonNum() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        User user = userMapper.selectById(userid);
        //查询用户以及直属子节点
        //List<String> userAndDescendants = userGradationService.getUserAndDescendants(user.getUserName());
        //return userAndDescendants.size()-1;
        List<String> userAndDescendants = userGradationService.getDirectDescendants(user.getUserName());
        return userAndDescendants.size();
    }

    /**
     * 获取自己和团队累计的交易额等其它信息
     *
     * @return
     */
    @Override
    public ResponseResult<AccumulatedTotal> getMyselfDealMoneyAndOther() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();

        User user = userMapper.selectById(userid);
        //查询用户及其子节点
        //List<String> userAndDescendants = userGradationService.getUserAndDescendants(user.getUserName());
        List<String> userAndDescendants = userGradationService.getDirectDescendants(user.getUserName());
        userAndDescendants.add(loginUser.getUser().getUserName());

        List<User> userList = new ArrayList<>();
        for (String userName : userAndDescendants) {
            User subUser = userMapper.selectByUserNameToUser(userName);
            if (subUser != null)
                userList.add(subUser);
        }
        BigDecimal money = BigDecimal.ZERO;

        for (User un : userList) {
            List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(un.getId());
            if (sysPosTerminalList == null || sysPosTerminalList.isEmpty()) {
                System.out.println("警告，" + un.getUserName() + " 未绑定pos机");
            }

            for (SysPosTerminal sysPosTerminal : sysPosTerminalList) {
                if (sysPosTerminal == null) {
                    // 可以考虑记录日志，但不返回，继续处理其他项
                    continue;
                }
                BigDecimal thisMonthMoney = commercialTenantOrderZFMapper.selectBySnMoney(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz());
                if (thisMonthMoney != null) {
                    money = money.add(thisMonthMoney);
                }
            }
        }

        AccumulatedTotal accumulatedTotal = new AccumulatedTotal();
        accumulatedTotal.setAccumulatedTotalMoney(money);
        accumulatedTotal.setAccumulatedTotalActivatePosNum(selectOurPosActivateNum());
        int t = selectOurPersonNum();
        accumulatedTotal.setAccumulatedTotalOurPersonnelNum(t);

        return new ResponseResult<>(200, "查询成功！", accumulatedTotal);
    }

    /**
     * 根据用户名查询一个人的pos机，激活的数量
     *
     * @param userName
     * @return
     */
    @Override
    public Integer selectByUserNameActivatePosNum(String userName) {
        User user = userMapper.selectByUserNameToUser(userName);
        if (user == null) {
            return 0;
        }
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(user.getId());
        return sysPosTerminalList.size();
    }


    /**
     * 根据用户名查询一个人的pos机，激活的数量，当前月的
     *
     * @param userName
     * @return
     */
    @Override
    public Integer selectByUserNameActivatePosNumThisMonth(String userName) {
        User user = userMapper.selectByUserNameToUser(userName);
        if (user == null) {
            return 0;
        }
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidThisMonth(user.getId());
        return sysPosTerminalList.size();
    }


    /**
     * 小伙伴本月的信息
     *
     * @return
     */
    @Override
    public ResponseResult<DealPerformance> getOurThisMonthDealMoneyAndOther() {

        DealPerformance result = new DealPerformance();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();

        User user = userMapper.selectById(userid);
        //查询用户已经所有下级的用户名
        List<String> userAndDescendants = userGradationService.getUserAndDescendants(user.getUserName());
        userAndDescendants.remove(user.getUserName());
        List<User> userList = new ArrayList<>();
        for (String userName : userAndDescendants) {
            User subUser = userMapper.selectByUserNameToUser(userName);
            if (subUser != null)
                userList.add(subUser);
        }
        BigDecimal money = BigDecimal.ZERO;

        for (User un : userList) {
            List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(un.getId());
            if (sysPosTerminalList == null || sysPosTerminalList.isEmpty()) {
                System.out.println("警告，" + un.getUserName() + " 未绑定pos机");
            }

            for (SysPosTerminal sysPosTerminal : sysPosTerminalList) {
                if (sysPosTerminal == null) {
                    // 可以考虑记录日志，但不返回，继续处理其他项
                    continue;
                }
                BigDecimal thisMonthMoney = commercialTenantOrderZFMapper.selectBySnThisMonthMoney(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz());
                if (thisMonthMoney != null) {
                    money = money.add(thisMonthMoney);
                }
            }
        }

        //自己与所有下级的本月交易金额
        result.setMoney(money);

        //自己和下级邀请的人---------------------------------
        //获取自己的所有下级，利用set去重
        List<String> userAndDescendantsList = userGradationService.getUserAndDescendants(user.getUserName());
        //直属下级不算新的伙伴中
        List<String> directDescendantList = userGradationService.getDirectDescendants(user.getUserName());
        userAndDescendantsList.remove(loginUser.getUser().getUserName());
        result.setAccumulativeTotalPartner(userAndDescendantsList.size() - directDescendantList.size()); //累计邀请的人


        //当前月，自己和下级邀请的人---------------------------------

        // 重命名为 allDescendantsList
        List<String> allDescendantsList = userGradationService.getUserAndDescendants(user.getUserName());
        // 重命名为 directDescendantsList
        List<String> directDescendantsList = userGradationService.getDirectDescendants(user.getUserName());
        // 从 allDescendantsList 中移除 user.getUserName()
        allDescendantsList.remove(user.getUserName());
        // 从 allDescendantsList 中移除所有在 directDescendantsList 中的元素
        allDescendantsList.removeAll(directDescendantsList);
        // 使用 Set 去除重复项
        Set<String> uniqueSet = new HashSet<>(allDescendantsList);
        // 将去重后的集合转换回 List
        List<String> uniqueList = new ArrayList<>(uniqueSet);

        //当前月，自己和下级邀请的人，
        int newPartnerNum = 0;
        for (String un : uniqueList) {
            User curUser = userMapper.selectByUserNameToUser(un);
            if (curUser == null) {
                continue;
            }
            Date userCreateTime = curUser.getCreateTime();
            if (userCreateTime != null) {
                // 获取当前时间
                Calendar now = Calendar.getInstance();
                // 设置为当前月份的第一天
                now.set(Calendar.DAY_OF_MONTH, 1);
                Date firstDayOfMonth = now.getTime();

                // 比较 userCreateTime 是否在当前月份第一天之后
                if (userCreateTime.after(firstDayOfMonth)) {
                    newPartnerNum++;
                }
            }
        }
        result.setNewPartner(newPartnerNum);


        //下面是自己与直属下级历史激活数量-------------------------------
        int historyActivateNum = 0;
        for (String un : userAndDescendantsList) {
            historyActivateNum += selectByUserNameActivatePosNum(un);
        }
        result.setAccumulativeTotalActivatePos(historyActivateNum);

        int thisMonthActivateNum = 0;
        for (String un : userAndDescendantsList) {
            thisMonthActivateNum += selectByUserNameActivatePosNumThisMonth(un);
        }
        result.setNewActivatePos(thisMonthActivateNum);

        return new ResponseResult<>(200, "查询成功！", result);
    }

    /**
     * 根据用户名查询新增商户（pos机）当天的
     *
     * @param userName
     * @return
     */
    @Override
    public Integer selectNewlyIncreasedPosNumToday(String userName, Date queryDate) {
        User user = userMapper.selectByUserNameToUser(userName);
        if (user == null) {
            return 0;
        }
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidToday(user.getId(), queryDate);
        return sysPosTerminalList.size();
    }

    /**
     * 根据用户名查询新增伙伴、当天的
     *
     * @param userName
     * @return
     */
    @Override
    public Integer selectNewlyIncreasedPersonNumToday(String userName, Date queryDate) {
        User user = userMapper.selectByUserNameToUser(userName);
        if (user == null) {
            return 0;
        }
        List<User> userList = userMapper.selectByMyInvitationTodayUserList(user.getMyInvitationCode(), queryDate);
        return userList.size();
    }

    /**
     * 根据用户名查询新增伙伴、当月的
     *
     * @param userName
     * @return
     */
    @Override
    public Integer selectNewlyIncreasedPersonNumMoney(String userName, Date queryDate) {
        User user = userMapper.selectByUserNameToUser(userName);
        if (user == null) {
            return 0;
        }
        List<User> userList = userMapper.selectByMyInvitationMonthUserList(user.getMyInvitationCode(), queryDate);
        return userList.size();
    }

    /**
     * 根据用户名查询新增商户（pos机）指定月的
     *
     * @param userName
     * @return
     */
    @Override
    public Integer selectNewlyIncreasedPosNumMoneyMonth(String userName, Date queryDate) {
        User user = userMapper.selectByUserNameToUser(userName);
        if (user == null) {
            return 0;
        }
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidMonth(user.getId(), queryDate);
        return sysPosTerminalList.size();
    }

    /**
     * 对DealPerformanceRequestQuery.resultList.time排序
     *
     * @param resultList
     */
    public static void sortDealPerformanceByTimeDescending(List<DealPerformance> resultList) {
        Collections.sort(resultList, new Comparator<DealPerformance>() {
            @Override
            public int compare(DealPerformance o1, DealPerformance o2) {
                // 时间越晚的排在前面
                return o2.getTime().compareTo(o1.getTime());
            }
        });
    }

    /**
     * 返回指定日期指点的月，虽少十二个天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Date> getDatesBetweenDay(Date startDate, Date endDate) {
        // 使用Calendar实例来增加日期
        Calendar calendarTemp = Calendar.getInstance();
        calendarTemp.setTime(endDate);
        calendarTemp.add(Calendar.DAY_OF_YEAR, 1); // 加一天
        endDate = calendarTemp.getTime();

        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        // 添加从startDate到endDate的日期，包括这两天
        while (!calendar.getTime().after(endDate)) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // 如果不足12天，继续添加startDate之前的日期直到达到至少12天
        calendar.setTime(startDate); // 重新设置为开始日期
        while (dates.size() < 12) {
            calendar.add(Calendar.DAY_OF_YEAR, -1); // 向前（过去）添加一天
            dates.add(0, calendar.getTime()); // 在列表的开头添加日期
        }

        return dates;
    }


    /**
     * 查询指定日业绩中的个人业绩中的日维度，查询自己和直属下级的交易信息
     *
     * @param query
     * @return
     */
    @Override
    public ResponseResult<DealPerformanceRequestQuery> selectPageMyselfTodayMsg(DealPerformanceRequestQuery query) {
        query.setResultList(new ArrayList<>());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User curUser = loginUser.getUser();

        Date createTime = curUser.getCreateTime();

        Date today = new Date();

        // 生成从createTime到今天的所有日期
        List<Date> allDates = getDatesBetweenDay(createTime, today);

        // 输出这些日期
        for (Date queryDate : allDates) {
            //累计伙伴
            Integer accumulativeTotalPartner = getThisMonthDealMoneyAndOther().getData().getAccumulativeTotalPartner();

            //当日多少交易额
            BigDecimal money = BigDecimal.ZERO;


            //如果自己没有Pos机，所以就没有交易额，本月交易额直接是0
            List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(loginUser.getUser().getId());
            if (sysPosTerminalList == null || sysPosTerminalList.isEmpty()) {
            } else {
                for (SysPosTerminal sysPosTerminal : sysPosTerminalList) {
                    if (sysPosTerminal == null) {
                        // 可以考虑记录日志，但不返回，继续处理其他项
                        continue;
                    }
                    BigDecimal thisMonthMoney = commercialTenantOrderZFMapper.selectBySnTodayMoney(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz(), queryDate);
                    if (thisMonthMoney != null) {
                        money = money.add(thisMonthMoney);
                    }
                }
            }

            //当日新增小伙伴
            Integer todayNewPerson = selectNewlyIncreasedPersonNumToday(loginUser.getUser().getUserName(), queryDate);

            //当日新增机器
            Integer todayNewPos = 0;

            //新增商户（pos机），自己和所有下级的
            List<String> userAndDescendants = userGradationService.getUserAndDescendants(loginUser.getUser().getUserName());

            for (String un : userAndDescendants) {
                todayNewPos += selectNewlyIncreasedPosNumToday(un, queryDate);
            }

            DealPerformance dealPerformance = new DealPerformance();
            dealPerformance.setTime(queryDate);
            dealPerformance.setMoney(money);
            dealPerformance.setNewPartner(todayNewPerson);
            dealPerformance.setNewActivatePos(todayNewPos);
            dealPerformance.setAccumulativeTotalPartner(accumulativeTotalPartner);

            query.getResultList().add(dealPerformance);
        }

        sortDealPerformanceByTimeDescending(query.getResultList());

        query.setCount(query.getResultList().size());
        query.setResultList(query.getMyselfResultList());
        return new ResponseResult(200, "查询成功！", query);
    }

    /**
     * 获取从指定的开始日期到结束日期的月间隔日期列表，保证至少十二个月。
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 月间隔的日期列表
     */
    public static List<Date> getDatesWithMonthInterval(Date startDate, Date endDate) {

        // 使用Calendar类来增加月份
        Calendar calendarTemp = Calendar.getInstance();
        calendarTemp.setTime(endDate);
        calendarTemp.add(Calendar.MONTH, 1);

        // 获取增加一个月后的日期
        endDate = calendarTemp.getTime();

        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        // 计算 startDate 和 endDate 之间的月份差异
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        int monthsBetween = monthsBetweenDates(calendar, endCalendar);

        // 如果月份差异小于12个月，从 startDate 开始向前推至少12个月
        if (monthsBetween < 12) {
            calendar.add(Calendar.MONTH, -12 + monthsBetween);
        }

        // 填充12个月的日期
        for (int i = 0; i < 12; i++) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
        }

        return dates;
    }

    // 计算两个日期之间的完整月份差异
    private static int monthsBetweenDates(Calendar start, Calendar end) {
        int diffYear = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        return diffMonth;
    }


    /**
     * 个人业绩的月维度
     *
     * @param query
     * @return
     */
    @Override
    public ResponseResult<DealPerformanceRequestQuery> selectPageMyselfThisMonthMsg(DealPerformanceRequestQuery query) {
        query.setResultList(new ArrayList<>());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User curUser = loginUser.getUser();

        Date createTime = curUser.getCreateTime();

        Date today = new Date();

        // 生成从createTime到今天的所有日期
        List<Date> allDates = getDatesWithMonthInterval(createTime, today);

        // 输出这些日期
        for (Date queryDate : allDates) {
            //累计伙伴
            Integer accumulativeTotalPartner = getThisMonthDealMoneyAndOther().getData().getAccumulativeTotalPartner();

            //当日多少交易额
            BigDecimal money = BigDecimal.ZERO;


            //如果自己没有Pos机，所以就没有交易额，本月交易额直接是0
            List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(loginUser.getUser().getId());
            if (sysPosTerminalList == null || sysPosTerminalList.isEmpty()) {
            } else {
                for (SysPosTerminal sysPosTerminal : sysPosTerminalList) {
                    if (sysPosTerminal == null) {
                        // 可以考虑记录日志，但不返回，继续处理其他项
                        continue;
                    }
                    BigDecimal thisMonthMoney = commercialTenantOrderZFMapper.selectBySnSpecifiedYearCurrentMonth(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz(), queryDate);
                    if (thisMonthMoney != null) {
                        money = money.add(thisMonthMoney);
                    }
                }
            }

            //当月新增小伙伴
            Integer todayNewPerson = selectNewlyIncreasedPersonNumMoney(loginUser.getUser().getUserName(), queryDate);

            //当月新增机器
            Integer todayNewPos = 0;

            //新增商户（pos机），自己和所有下级的
            List<String> userAndDescendants = userGradationService.getUserAndDescendants(loginUser.getUser().getUserName());

            for (String un : userAndDescendants) {
                todayNewPos += selectNewlyIncreasedPosNumMoneyMonth(un, queryDate);
            }

            DealPerformance dealPerformance = new DealPerformance();
            dealPerformance.setTime(queryDate);
            dealPerformance.setMoney(money);
            dealPerformance.setNewPartner(todayNewPerson);
            dealPerformance.setNewActivatePos(todayNewPos);
            dealPerformance.setAccumulativeTotalPartner(accumulativeTotalPartner);

            query.getResultList().add(dealPerformance);
        }

        sortDealPerformanceByTimeDescending(query.getResultList());

        query.setCount(query.getResultList().size());
        query.setResultList(query.getMyselfResultList());
        return new ResponseResult(200, "查询成功！", query);
    }


    /**
     * 查询指定月业绩中的团队业绩中的日维度，查询自己所有的下级，不包括自己
     *
     * @param query
     * @return
     */
    @Override
    public ResponseResult<DealPerformanceRequestQuery> selectPageOurTodayMsg(DealPerformanceRequestQuery query) {
        query.setResultList(new ArrayList<>());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User curUser = loginUser.getUser();

        Date createTime = curUser.getCreateTime();

        Date today = new Date();

        // 生成从createTime到今天的所有日期
        List<Date> allDates = getDatesBetweenDay(createTime, today);

        //累计伙伴
        Integer accumulativeTotalPartner = getOurThisMonthDealMoneyAndOther().getData().getAccumulativeTotalPartner();

        // 输出这些日期
        for (Date queryDate : allDates) {

            //当日多少交易额
            BigDecimal money = BigDecimal.ZERO;

            //当日新增小伙伴
            Integer todayNewPerson = 0;

            //当日新增机器
            Integer todayNewPos = 0;

            //新增商户（pos机），自己和所有下级的
            List<String> userAndDescendants = userGradationService.getUserAndDescendants(loginUser.getUser().getUserName());
            userAndDescendants.remove(loginUser.getUser().getUserName());
            for (String un : userAndDescendants) {
                todayNewPos += selectNewlyIncreasedPosNumToday(un, queryDate);
                //当日新增小伙伴
                todayNewPerson += selectNewlyIncreasedPersonNumToday(un, queryDate);

                //如果自己没有Pos机，所以就没有交易额，本月交易额直接是0
                List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(userMapper.selectByUserNameToUser(un).getId());
                if (sysPosTerminalList == null || sysPosTerminalList.isEmpty()) {
                } else {
                    for (SysPosTerminal sysPosTerminal : sysPosTerminalList) {
                        if (sysPosTerminal == null) {
                            // 可以考虑记录日志，但不返回，继续处理其他项
                            continue;
                        }
                        BigDecimal thisMonthMoney = commercialTenantOrderZFMapper.selectBySnTodayMoney(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz(), queryDate);
                        if (thisMonthMoney != null) {
                            money = money.add(thisMonthMoney);
                        }
                    }
                }
            }

            DealPerformance dealPerformance = new DealPerformance();
            dealPerformance.setTime(queryDate);
            dealPerformance.setMoney(money);
            dealPerformance.setNewPartner(todayNewPerson);
            dealPerformance.setNewActivatePos(todayNewPos);
            dealPerformance.setAccumulativeTotalPartner(accumulativeTotalPartner);

            query.getResultList().add(dealPerformance);
        }

        sortDealPerformanceByTimeDescending(query.getResultList());

        query.setCount(query.getResultList().size());
        query.setResultList(query.getMyselfResultList());
        return new ResponseResult(200, "查询成功！", query);
    }

    /**
     * 伙伴的月维度
     *
     * @param query
     * @return
     */
    @Override
    public ResponseResult<DealPerformanceRequestQuery> selectPageOurMonthMsg(DealPerformanceRequestQuery query) {
        query.setResultList(new ArrayList<>());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User curUser = loginUser.getUser();

        Date createTime = curUser.getCreateTime();

        Date today = new Date();

        // 生成从createTime到今天的所有日期，月
        List<Date> allDates = getDatesWithMonthInterval(createTime, today);

        //累计伙伴
        Integer accumulativeTotalPartner = getOurThisMonthDealMoneyAndOther().getData().getAccumulativeTotalPartner();

        // 输出这些日期
        for (Date queryDate : allDates) {

            //当日多少交易额
            BigDecimal money = BigDecimal.ZERO;

            //当日新增小伙伴
            Integer todayNewPerson = 0;

            //当日新增机器
            Integer todayNewPos = 0;

            //新增商户（pos机），自己和所有下级的
            List<String> userAndDescendants = userGradationService.getUserAndDescendants(loginUser.getUser().getUserName());
            userAndDescendants.remove(loginUser.getUser().getUserName());
            for (String un : userAndDescendants) {
                todayNewPos += selectNewlyIncreasedPosNumMoneyMonth(un, queryDate);
                //当日新增小伙伴
                todayNewPerson += selectNewlyIncreasedPersonNumMoney(un, queryDate);

                //如果自己没有Pos机，所以就没有交易额，本月交易额直接是0
                List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(userMapper.selectByUserNameToUser(un).getId());
                if (sysPosTerminalList == null || sysPosTerminalList.isEmpty()) {
                } else {
                    for (SysPosTerminal sysPosTerminal : sysPosTerminalList) {
                        if (sysPosTerminal == null) {
                            // 可以考虑记录日志，但不返回，继续处理其他项
                            continue;
                        }
                        BigDecimal thisMonthMoney = commercialTenantOrderZFMapper.selectBySnSpecifiedYearCurrentMonth(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz(), queryDate);
                        if (thisMonthMoney != null) {
                            money = money.add(thisMonthMoney);
                            System.out.print(sysPosTerminal.getMachineNo() + "   " + sysPosTerminal.getClazz() + "  " + queryDate);
                            System.out.println("  money加:" + thisMonthMoney);
                        }
                    }
                }
            }

            DealPerformance dealPerformance = new DealPerformance();
            dealPerformance.setTime(queryDate);
            dealPerformance.setMoney(money);
            System.out.println("money=======" + money);
            System.out.println();
            dealPerformance.setNewPartner(todayNewPerson);
            dealPerformance.setNewActivatePos(todayNewPos);
            dealPerformance.setAccumulativeTotalPartner(accumulativeTotalPartner);

            query.getResultList().add(dealPerformance);
        }

        sortDealPerformanceByTimeDescending(query.getResultList());

        query.setCount(query.getResultList().size());
        query.setResultList(query.getMyselfResultList());
        return new ResponseResult(200, "查询成功！", query);
    }



    /**
     * 查询指定月业绩中的累计业绩中的日维度，查询自己所有的下级，不包括自己
     *
     * @param query
     * @return
     */
    @Override
    public ResponseResult<DealPerformanceRequestQuery> selectPageAllTodayMsg(DealPerformanceRequestQuery query) {
        query.setResultList(new ArrayList<>());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User curUser = loginUser.getUser();

        Date createTime = curUser.getCreateTime();

        Date today = new Date();

        // 生成从createTime到今天的所有日期
        List<Date> allDates = getDatesBetweenDay(createTime, today);

        Collections.sort(allDates, new Comparator<Date>() {
            @Override
            public int compare(Date date1, Date date2) {
                // 降序排列
                return date2.compareTo(date1);
            }
        });

        // 多少天就有多少条
        query.setCount(allDates.size());

        int pageNumber = query.getPageNumber(); // 当前页码
        int quantity = query.getQuantity(); // 每页显示的记录数
        int totalSize = allDates.size(); // 总记录数


        // 计算最大页数
        int maxPageNumber = (totalSize + quantity - 1) / quantity;

        // 检查请求的页码是否超过最大页数
        if (pageNumber > maxPageNumber) {
            // 如果超过了，返回空的结果集
            query.setResultList(new ArrayList<>());
        } else {
            // 计算当前页的起始索引和结束索引
            int startIndex = (pageNumber - 1) * quantity;
            int endIndex = Math.min(startIndex + quantity, totalSize);

            // 获取当前页的子列表
            allDates = allDates.subList(startIndex, endIndex);
        }

        //累计伙伴
        Integer accumulativeTotalPartner = getOurThisMonthDealMoneyAndOther().getData().getAccumulativeTotalPartner();

        // 输出这些日期
        for (Date queryDate : allDates) {

            //当日多少交易额
            BigDecimal money = BigDecimal.ZERO;

            //当日新增小伙伴
            Integer todayNewPerson = 0;

            //当日新增机器
            Integer todayNewPos = 0;

            //新增商户（pos机），自己和直属下级
            List<String> userAndDescendants = userGradationService.getDirectDescendants(loginUser.getUser().getUserName());
            userAndDescendants.add(curUser.getUserName());
            for (String un : userAndDescendants) {
                todayNewPos += selectNewlyIncreasedPosNumToday(un, queryDate);
                //当日新增小伙伴
                todayNewPerson += selectNewlyIncreasedPersonNumToday(un, queryDate);

                //如果自己没有Pos机，所以就没有交易额，本月交易额直接是0
                List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(userMapper.selectByUserNameToUser(un).getId());
                if (sysPosTerminalList == null || sysPosTerminalList.isEmpty()) {
                } else {
                    for (SysPosTerminal sysPosTerminal : sysPosTerminalList) {
                        if (sysPosTerminal == null) {
                            // 可以考虑记录日志，但不返回，继续处理其他项
                            continue;
                        }
                        BigDecimal thisMonthMoney = commercialTenantOrderZFMapper.selectBySnTodayMoney(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz(), queryDate);
                        if (thisMonthMoney != null) {
                            money = money.add(thisMonthMoney);
                        }
                    }
                }
            }

            DealPerformance dealPerformance = new DealPerformance();
            dealPerformance.setTime(queryDate);
            dealPerformance.setMoney(money);
            dealPerformance.setNewPartner(todayNewPerson);
            dealPerformance.setNewActivatePos(todayNewPos);
            dealPerformance.setAccumulativeTotalPartner(accumulativeTotalPartner);
            query.getResultList().add(dealPerformance);
        }

        sortDealPerformanceByTimeDescending(query.getResultList());

        return new ResponseResult(200, "查询成功！", query);
    }

    @Override
    public ResponseResult<DealPerformanceRequestQuery> selectPageAllMonthMsg(DealPerformanceRequestQuery query) {
        query.setResultList(new ArrayList<>());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User curUser = loginUser.getUser();

        Date createTime = curUser.getCreateTime();

        Date today = new Date();

        // 生成从createTime到今天的所有日期，月
        List<Date> allDates = getDatesWithMonthInterval(createTime, today);

        //累计伙伴
        Integer accumulativeTotalPartner = getOurThisMonthDealMoneyAndOther().getData().getAccumulativeTotalPartner();

        // 输出这些日期
        for (Date queryDate : allDates) {

            //当日多少交易额
            BigDecimal money = BigDecimal.ZERO;

            //当日新增小伙伴
            Integer todayNewPerson = 0;

            //当日新增机器
            Integer todayNewPos = 0;

            //新增商户（pos机），自己和所有下级的
            List<String> userAndDescendants = userGradationService.getUserAndDescendants(loginUser.getUser().getUserName());
            for (String un : userAndDescendants) {
                todayNewPos += selectNewlyIncreasedPosNumMoneyMonth(un, queryDate);
                //当日新增小伙伴
                todayNewPerson += selectNewlyIncreasedPersonNumMoney(un, queryDate);

                //如果自己没有Pos机，所以就没有交易额，本月交易额直接是0
                List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(userMapper.selectByUserNameToUser(un).getId());
                if (sysPosTerminalList == null || sysPosTerminalList.isEmpty()) {
                } else {
                    for (SysPosTerminal sysPosTerminal : sysPosTerminalList) {
                        if (sysPosTerminal == null) {
                            // 可以考虑记录日志，但不返回，继续处理其他项
                            continue;
                        }
                        BigDecimal thisMonthMoney = commercialTenantOrderZFMapper.selectBySnSpecifiedYearCurrentMonth(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz(), queryDate);
                        if (thisMonthMoney != null) {
                            money = money.add(thisMonthMoney);
                            System.out.print(sysPosTerminal.getMachineNo() + "   " + sysPosTerminal.getClazz() + "  " + queryDate);
                            System.out.println("  money加:" + thisMonthMoney);
                        }
                    }
                }
            }

            DealPerformance dealPerformance = new DealPerformance();
            dealPerformance.setTime(queryDate);
            dealPerformance.setMoney(money);
            System.out.println("money=======" + money);
            System.out.println();
            dealPerformance.setNewPartner(todayNewPerson);
            dealPerformance.setNewActivatePos(todayNewPos);
            dealPerformance.setAccumulativeTotalPartner(accumulativeTotalPartner);

            query.getResultList().add(dealPerformance);
        }

        sortDealPerformanceByTimeDescending(query.getResultList());

        query.setCount(query.getResultList().size());
        query.setResultList(query.getMyselfResultList());
        return new ResponseResult(200, "查询成功！", query);
    }
}
