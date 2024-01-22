package xin.admin.service.fundsFlowManagement.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.controller.fundsFlowManagement.push.ZFInformPush;
import xin.admin.controller.sse.NotificationSSEController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.CommercialTenantOrderZF;
import xin.admin.domain.fundsFlowManagement.SetupPosRate;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.fundsFlowManagement.query.CommercialTenantOrderZFRequestQuery;
import xin.admin.domain.serviceCharge.SysPosDealServiceCharge;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.fundsFlowManagement.CommercialTenantOrderZFMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.service.fundsFlowManagement.CommercialTenantOrderZFService;
import xin.admin.service.fundsFlowManagement.SetupPosRateService;
import xin.admin.service.serviceCharge.SysSomePosDealServiceChargeService;
import xin.admin.service.sse.impl.NotificationSSEServiceImpl;
import xin.admin.service.userInfomation.UserService;
import xin.admin.utils.TimeUtils;
import xin.common.domain.User;
import xin.h5.service.performance.PerformanceService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class CommercialTenantOrderZFServiceImpl implements CommercialTenantOrderZFService {

    @Autowired
    CommercialTenantOrderZFMapper commercialTenantOrderZFMapper;

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Autowired
    PerformanceService performanceService;

    // 交易服务费相关的
    @Autowired
    SysSomePosDealServiceChargeService sysSomePosDealServiceChargeService;

    @Autowired
    SetupPosRateService setupPosRateService;

    @Autowired
    NotificationSSEController notificationSSEController;

    /**
     * 判断此次交易是否成功，true成功
     *
     * @param ctoZF
     * @return
     */
    public Boolean isDealSucceed(CommercialTenantOrderZF ctoZF) {
        String respCode = ctoZF.getSysRespCode();  //应答码，是否成功

        //如果不是成功的就不分润了
        if ("00".equals(respCode) || "10".equals(respCode) || "11".equals(respCode) ||
                "A2".equals(respCode) || "A4".equals(respCode) || "A5".equals(respCode) ||
                "A6".equals(respCode)) {
            return true;
        }
        return false;
    }

    @Override
    public ResponseResult add(ZFInformPush<CommercialTenantOrderZF> commercialTenantOrderZFPush) {
        for (CommercialTenantOrderZF ctoz : commercialTenantOrderZFPush.getDataList()) {

            //如果参考号Rrn已经存在就跳过存入数据库
            if (commercialTenantOrderZFMapper.existsByRrn(ctoz.getRrn())) {
                continue;
            }

            //获取交易信息对应的pos机
            SysPosTerminal sysPosTerminal = sysPosTerminalMapper.selectByMachineNoToMachineAndClazzMsg(ctoz.getTermSn(), ctoz.getTermModel());
            if (sysPosTerminal != null) {
                //pos机对应的数量
                User user = userMapper.selectById(sysPosTerminal.getUid());
                if (user != null && ctoz != null) {
                    Float transactionVolume = user.getTransactionVolume();
                    BigDecimal amount = ctoz.getAmount();
                    if (transactionVolume != null && amount != null) {
                        //pos机的交易额
                        BigDecimal transactionVolumeBigDecimal = BigDecimal.valueOf(transactionVolume);
                        BigDecimal newTransactionVolume = transactionVolumeBigDecimal.add(amount);
                        user.setTransactionVolume(newTransactionVolume.floatValue());
                        userMapper.updateById(user);
                    }
                }
            }

            try {
                ctoz.setCreatetime(TimeUtils.formatDateTime(commercialTenantOrderZFPush.getSendTime()));
            } catch (ParseException e) {
                ctoz.setCreatetime(TimeUtils.formatDate(new Date()));
                throw new RuntimeException(e);
            }
            ctoz.setOAmount(ctoz.getAmount());
            ctoz.setLogno(ctoz.getCreatetime() + ctoz.getRrn());

            //判断是否是交易的成功，并且是第一次推送
            if (isDealSucceed(ctoz) && commercialTenantOrderZFMapper.selectByRrnIsExist(ctoz.getRrn()) == 0) {

                // 更新该pos机的当日交易信息
                performanceService.periodicUpdate(ctoz);

                //根据交易流水进行分润
                userService.shareBenefit(ctoz);

                try {
                    QueryWrapper<SysPosTerminal> qw = new QueryWrapper<>();
                    qw.eq("machine_no", ctoz.getTermSn());
                    qw.eq("clazz", ctoz.getTermModel());
                    SysPosTerminal sysPosTerminal1 = sysPosTerminalMapper.selectOne(qw);

                    // 防止有些机器不存在与仓库
                    if (sysPosTerminal1 != null) {
                        List<SysPosDealServiceCharge> resultList = sysSomePosDealServiceChargeService.selectBySptId(sysPosTerminal1.getId()).getData();
                        BigDecimal multiplied = ctoz.getAmount().divide(new BigDecimal("100"));
                        float money = multiplied.floatValue();

                        for (SysPosDealServiceCharge spd : resultList) {
                            if (money >= spd.getStartInterval() && money <= spd.getEndInterval()) {
                                SetupPosRate setupPosRate = new SetupPosRate();
                                setupPosRate.setId(sysPosTerminal1.getId());
                                setupPosRate.setPosCharge(String.valueOf(spd.getFeePerTransaction()));
                                setupPosRate.setVipCharge("0");
                                setupPosRate.setSimCharge("0");
                                System.out.println("rrn:" + ctoz.getRrn() + " 扣款" + setupPosRate.getPosCharge());
                                System.out.println("结果：" + setupPosRateService.set(setupPosRate));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //保存交易流水
                commercialTenantOrderZFMapper.insert(ctoz);

                // 更新大屏幕的数据
                NotificationSSEServiceImpl.data.setHistoricalTransactionVolume(
                        NotificationSSEServiceImpl.data.getHistoricalTransactionVolume().add(
                                ctoz.getAmount().divide(new BigDecimal("100"))));

                NotificationSSEServiceImpl.data.setHistoricalTransactionCount(NotificationSSEServiceImpl.data.getHistoricalTransactionCount() + 1);

                // 设置今日交易金额
                NotificationSSEServiceImpl.data.setTodayTransactionAmount(commercialTenantOrderZFMapper.sumAmountToday().divide(new BigDecimal("100")));

                // 设置今日交易笔数
                NotificationSSEServiceImpl.data.setTodayTransactionCount(commercialTenantOrderZFMapper.sumCountToday().intValue());

                notificationSSEController.sendNotification();
            }
        }
        return new ResponseResult(200, "推送成功！");
    }

    @Override
    public ResponseResult<CommercialTenantOrderZFRequestQuery> list(CommercialTenantOrderZFRequestQuery query) {

        // 构建查询条件
        QueryWrapper<CommercialTenantOrderZF> wrapper = new QueryWrapper<>();

        if (query.getCommercialTenantOrderZF() != null) {
            // 检查tranCode和termSn是否不为空
            if (query.getCommercialTenantOrderZF().getTranCode() != null &&
                    !query.getCommercialTenantOrderZF().getTranCode().isEmpty()) {
                wrapper.eq("tranCode", query.getCommercialTenantOrderZF().getTranCode());
            }

            if (query.getCommercialTenantOrderZF().getTermSn() != null &&
                    !query.getCommercialTenantOrderZF().getTermSn().isEmpty()) {
                wrapper.eq("termSn", query.getCommercialTenantOrderZF().getTermSn());
            }

            // 检查创建时间是否在指定范围内
            if (query.getStartTime() != null && query.getEndTime() != null) {
                wrapper.ge("createtime", query.getStartTime()) // 大于等于startTime
                        .le("createtime", query.getEndTime());  // 小于等于endTime
            }
        }

        // 根据ID降序排列
        wrapper.orderByDesc("id");

        // 分页查询
        Page<CommercialTenantOrderZF> page = new Page<>(query.getPageNumber(), query.getQuantity());
        IPage<CommercialTenantOrderZF> resultPage = commercialTenantOrderZFMapper.selectPage(page, wrapper);

        // 设置返回结果
        query.setResultList(resultPage.getRecords());
        query.setCount((int) resultPage.getTotal());

        query.setMoney(commercialTenantOrderZFMapper.sumAmount());

        return new ResponseResult<>(200, "查询成功！", query);
    }

    /*public ResponseResult<CommercialTenantOrderZFRequestQuery> list(CommercialTenantOrderZFRequestQuery query) {
        // 构建查询条件
        QueryWrapper<CommercialTenantOrderZF> wrapper = new QueryWrapper<>();

        if (query.getCommercialTenantOrderZF() != null) {
            // 检查tranCode和termSn是否不为空
            if (query.getCommercialTenantOrderZF().getTranCode() != null &&
                    !query.getCommercialTenantOrderZF().getTranCode().isEmpty()) {
                wrapper.eq("tranCode", query.getCommercialTenantOrderZF().getTranCode());
            }

            if (query.getCommercialTenantOrderZF().getTermSn() != null &&
                    !query.getCommercialTenantOrderZF().getTermSn().isEmpty()) {
                wrapper.eq("termSn", query.getCommercialTenantOrderZF().getTermSn());
            }


            // 检查创建时间是否在指定范围内
            // 假设startTime和endTime也是字符串格式
            if (query.getStartTime() != null && query.getEndTime() != null) {
                wrapper.ge("createtime", query.getStartTime()) // 大于等于startTime
                        .le("createtime", query.getEndTime());  // 小于等于endTime
            }
        }

        // 根据ID降序排列
        wrapper.orderByDesc("id");

        // 分页查询
        Page<CommercialTenantOrderZF> page = new Page<>(query.getPageNumber(), query.getQuantity());
        IPage<CommercialTenantOrderZF> resultPage = commercialTenantOrderZFMapper.selectPage(page, wrapper);

        // 设置返回结果
        query.setResultList(resultPage.getRecords());
        query.setCount((int) resultPage.getTotal());

        query.setMoney(commercialTenantOrderZFMapper.sumAmount());

        return new ResponseResult<>(200, "查询成功！", query);
    }*/

}
