package xin.h5.service.myAlly.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.fundsFlowManagement.CommercialTenantOrderZFMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.common.domain.User;
import xin.h5.domain.myselfCommercialTenant.MyAllyMsg;
import xin.h5.domain.myselfCommercialTenant.query.MyAllyMsgRequestQuery;
import xin.h5.service.myAlly.MyAllyService;
import xin.level.service.UserGradationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MyAllyServiceImpl implements MyAllyService {

    @Autowired
    UserGradationService userGradationService;

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommercialTenantOrderZFMapper commercialTenantOrderZFMapper;


    /**
     * 判断日期是否是当前月，一月开始的
     *
     * @param updateDate
     * @return
     */
    public static boolean isDateFromFirstDayOfCurrentMonth(Date updateDate) {

        if (updateDate == null)
            return false;

        // 将 Date 转换为 LocalDate
        LocalDate updateLocalDate = updateDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // 获取当前月的第一天
        LocalDate firstDayCurrentMonth = LocalDate.now().withDayOfMonth(1);

        // 检查 updateDate 是否在当前月的第一天或之后
        return !updateLocalDate.isBefore(firstDayCurrentMonth);
    }

    /**
     * 查询我的盟友列表
     *
     * @param query
     * @return
     */
    @Override
    public ResponseResult<MyAllyMsgRequestQuery> queryMyAlly(MyAllyMsgRequestQuery query) {

        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<String> directDescendants = userGradationService.getDirectDescendants(curUser.getUserName());

        List<MyAllyMsg> resultList = new ArrayList<>();

        //遍历我的盟友
        for (String un : directDescendants) {

            User user = userMapper.selectByUserNameToUser(un);
            if (user == null) {

                System.out.println("用户：不存在！" + user);
                continue;
            }

            //查询姓名
            if (query.getMyAllyMsg() != null && query.getMyAllyMsg().getName() != null) {
                String name1 = user.getUserName();
                String name = query.getMyAllyMsg().getName();
                if (!name1.contains(name)) {
                    continue;
                }
            }


            MyAllyMsg myAllyMsg = new MyAllyMsg();

            myAllyMsg.setUserName(user.getUserName());

            //商户名字，真实的
            myAllyMsg.setName(user.getName());

            //本月交易额
            BigDecimal thisMonthMoney = BigDecimal.ZERO;

            //历史交易量，现在不要了
            BigDecimal historyMoney = BigDecimal.ZERO;

            int thisMonthNewNumber = 0;
            List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(user.getId());
            for (SysPosTerminal spt : sysPosTerminalList) {

                BigDecimal monthMoney = commercialTenantOrderZFMapper.selectBySnThisMonthMoney(spt.getMachineNo(), spt.getClazz());
                if (monthMoney != null)
                    thisMonthMoney = thisMonthMoney.add(monthMoney);

                /*if (query.getSort()) {
                    BigDecimal hMoney = commercialTenantOrderZFMapper.selectBySnHistoryMoney(spt.getMachineNo(), spt.getClazz());
                    historyMoney = historyMoney.add(hMoney);
                }*/

                //如果是本月新开的pos机
                if (isDateFromFirstDayOfCurrentMonth(spt.getUpdateTime())) {
                    thisMonthNewNumber++;
                }
            }

            //设置本月交易额
            myAllyMsg.setThisMonthMoney(thisMonthMoney);

            //设置本月新增商户
            myAllyMsg.setThisMonthNewMerchantNum(thisMonthNewNumber);

            //设置历史交易数据
            myAllyMsg.setHistoryMerchantNum(sysPosTerminalList.size());

            resultList.add(myAllyMsg);
        }


        query.setCount(resultList.size());
        if (query.getSort()) {
            resultList.sort((o1, o2) -> o2.getHistoryMerchantNum().compareTo(o1.getHistoryMerchantNum()));
        }

        // 分页处理
        int pageNumber = query.getPageNumber() != null ? query.getPageNumber() : 1;
        int quantity = query.getQuantity() != null ? query.getQuantity() : 10;
        int startIndex = (pageNumber - 1) * quantity;
        int endIndex = Math.min(startIndex + quantity, resultList.size());

        // 返回分页后的结果
        List<MyAllyMsg> pagedResultList = resultList.subList(startIndex, endIndex);
        query.setResultList(pagedResultList);

        return new ResponseResult<>(200, "查询成功！", query);
    }

    @Override
    public ResponseResult<MyAllyMsg> queryMyAllyByUserName(String allyUserName) {

        User user = userMapper.selectByUserNameToUser(allyUserName);


        MyAllyMsg myAllyMsg = new MyAllyMsg();

        myAllyMsg.setUserName(user.getUserName());

        //商户名字，真实的
        myAllyMsg.setName(user.getName());

/*        //本月交易额，现在不要了
        BigDecimal thisMonthMoney = BigDecimal.ZERO;*/

        //历史交易量
        BigDecimal historyMoney = BigDecimal.ZERO;

        int thisMonthNewNumber = 0;
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(user.getId());
        for (SysPosTerminal spt : sysPosTerminalList) {

/*            BigDecimal monthMoney = commercialTenantOrderZFMapper.selectBySnThisMonthMoney(spt.getMachineNo(), spt.getClazz());
            if (monthMoney != null)
                thisMonthMoney = thisMonthMoney.add(monthMoney);*/

            BigDecimal hMoney = commercialTenantOrderZFMapper.selectBySnHistoryMoney(spt.getMachineNo(), spt.getClazz());
            if (hMoney != null)
                historyMoney = historyMoney.add(hMoney);

            //如果是本月新开的pos机
            if (isDateFromFirstDayOfCurrentMonth(spt.getUpdateTime())) {
                thisMonthNewNumber++;
            }
        }


/*        //设置本月交易额
        myAllyMsg.setThisMonthMoney(thisMonthMoney);*/

        myAllyMsg.setHistoryMoney(historyMoney);

        //设置本月新增商户
        myAllyMsg.setThisMonthNewMerchantNum(thisMonthNewNumber);

        //设置累计交易额
        myAllyMsg.setHistoryMerchantNum(sysPosTerminalList.size());

        return new ResponseResult<>(200, "查询成功！", myAllyMsg);
    }
}
