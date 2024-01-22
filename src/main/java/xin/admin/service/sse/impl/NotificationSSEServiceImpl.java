package xin.admin.service.sse.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    CommercialTenantOrderZFMapper commercialTenantOrderZFMapper;

    @Autowired
    UserMapper userMapper;

    public static NotificationSSE data = new NotificationSSE();

    @Override
    @PostConstruct
    public NotificationSSE getUpdatedData() {

        // 设置已激活机具数量
        QueryWrapper<SysPosTerminal> activeMachinesCountQw = new QueryWrapper<>();
        activeMachinesCountQw.eq("type", "3");
        data.setActiveMachinesCount(sysPosTerminalMapper.selectCount(activeMachinesCountQw).intValue());

        QueryWrapper<SysPosTerminal> inactiveMachinesCountQw = new QueryWrapper<>();
        inactiveMachinesCountQw.ne("type", "3");

        // 设置未激活机具数量
        data.setInactiveMachinesCount(sysPosTerminalMapper.selectCount(inactiveMachinesCountQw).intValue());

        // 设置历史机具交易流水金额
        data.setHistoricalTransactionVolume(commercialTenantOrderZFMapper.sumAmount().divide(new BigDecimal("100")));

        // 设置历史交易笔数量
        data.setHistoricalTransactionCount((int) commercialTenantOrderZFMapper.countRows());

        QueryWrapper<User> currentVisitorsQW = new QueryWrapper<>();
        currentVisitorsQW.isNull("open_id");

        // 设置本站当前人数
        data.setCurrentVisitorsCount(userMapper.selectCount(currentVisitorsQW).intValue());

        // 设置今日交易金额
        data.setTodayTransactionAmount(commercialTenantOrderZFMapper.sumAmountToday().divide(new BigDecimal("100")));

        // 设置今日交易笔数
        data.setTodayTransactionCount(commercialTenantOrderZFMapper.sumCountToday().intValue());

        return data;
    }
}
