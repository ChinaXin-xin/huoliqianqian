package xin.admin.service.sse.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.controller.sse.NotificationSSEController;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.sse.NotificationSSE;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.fundsFlowManagement.CommercialTenantOrderZFMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.service.sse.NotificationSSEService;
import xin.common.domain.User;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class NotificationSSEServiceImpl implements NotificationSSEService {

    private final SysPosTerminalMapper sysPosTerminalMapper;
    private final CommercialTenantOrderZFMapper commercialTenantOrderZFMapper;
    private final UserMapper userMapper;

    @Autowired
    public NotificationSSEServiceImpl(SysPosTerminalMapper sysPosTerminalMapper,
                                      CommercialTenantOrderZFMapper commercialTenantOrderZFMapper,
                                      UserMapper userMapper) {
        this.sysPosTerminalMapper = sysPosTerminalMapper;
        this.commercialTenantOrderZFMapper = commercialTenantOrderZFMapper;
        this.userMapper = userMapper;
    }

    @Override
    @PostConstruct
    public NotificationSSE getUpdatedData() {

        // 设置已激活机具数量
        QueryWrapper<SysPosTerminal> activeMachinesCountQw = new QueryWrapper<>();
        activeMachinesCountQw.eq("type", "3");
        NotificationSSEController.data.setActiveMachinesCount(sysPosTerminalMapper.selectCount(activeMachinesCountQw).intValue());

        QueryWrapper<SysPosTerminal> inactiveMachinesCountQw = new QueryWrapper<>();
        inactiveMachinesCountQw.ne("type", "3");

        // 设置未激活机具数量
        NotificationSSEController.data.setInactiveMachinesCount(sysPosTerminalMapper.selectCount(inactiveMachinesCountQw).intValue());

        // 设置历史机具交易流水金额
        NotificationSSEController.data.setHistoricalTransactionVolume(commercialTenantOrderZFMapper.sumAmount().divide(new BigDecimal("100")));

        // 设置历史交易笔数量
        NotificationSSEController.data.setHistoricalTransactionCount((int) commercialTenantOrderZFMapper.countRows());

        QueryWrapper<User> currentVisitorsQW = new QueryWrapper<>();
        currentVisitorsQW.isNull("open_id");

        // 设置本站当前人数
        NotificationSSEController.data.setCurrentVisitorsCount(userMapper.selectCount(currentVisitorsQW).intValue());

        // 设置今日交易金额
        BigDecimal sumAmountToday = commercialTenantOrderZFMapper.sumAmountToday();
        if (sumAmountToday == null || sumAmountToday.equals(BigDecimal.ZERO)) {
            NotificationSSEController.data.setTodayTransactionAmount(BigDecimal.ZERO);
        } else {
            NotificationSSEController.data.setTodayTransactionAmount(sumAmountToday.divide(new BigDecimal("100")));
        }



        // 设置今日交易笔数
        NotificationSSEController.data.setTodayTransactionCount(commercialTenantOrderZFMapper.sumCountToday().intValue());

        return NotificationSSEController.data;
    }
}
