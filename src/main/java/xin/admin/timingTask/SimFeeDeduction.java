package xin.admin.timingTask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.*;
import xin.admin.mapper.fundsFlowManagement.SysFeeDeductionRecordMapper;
import xin.admin.mapper.fundsFlowManagement.SysFeeRateMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.service.fundsFlowManagement.SetupPosRateService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SimFeeDeduction {

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    SysFeeRateMapper sysFeeRateMapper;

    @Autowired
    SetupPosRateService setupPosRateService;

    @Autowired
    SysFeeDeductionRecordMapper sysFeeDeductionRecordMapper;

    // 每天凌晨1点执行
    @Scheduled(cron = "0 0 1 * * ?")
    public void deductFees() {
        // 查询已经激活的列表
        QueryWrapper<SysPosTerminal> qw = new QueryWrapper<>();
        qw.eq("type", "3");
        List<SysPosTerminal> activatePosList = sysPosTerminalMapper.selectList(qw);

        QueryWrapper<SysFeeRate> sfrQw = new QueryWrapper<>();
        sfrQw.eq("fee_rate_name", "流量费");
        SysFeeRate sysFeeRate = sysFeeRateMapper.selectOne(sfrQw);

        Integer interval = sysFeeRate.getIntervalDays(); // 间隔天数
        if (interval == null) {
            return;
        }

        // 应该扣多少钱
        String money = sysFeeRate.getFeeRate();

        Date curDate = new Date(); // 当前日期

        for (SysPosTerminal spt : activatePosList) {
            Date updateTime = spt.getUpdateTime(); // 获取更新时间
            // 计算当前日期与更新日期之间的天数差
            long diffInMillies = Math.abs(curDate.getTime() - updateTime.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            // 如果天数差大于或等于间隔，则输出提示
            if (diff >= interval) {
                SysFeeDeductionRecord sysFeeDeductionRecord = new SysFeeDeductionRecord();
                sysFeeDeductionRecord.setType("流量");
                sysFeeDeductionRecord.setAmount(money);

                SetupPosRate setupPosRate = new SetupPosRate();
                setupPosRate.setId(spt.getId());
                setupPosRate.setSimCharge(money);
                setupPosRate.setPosCharge("0");
                setupPosRate.setVipCharge("0");

                ResponseResult resResult = setupPosRateService.set(setupPosRate);

                if (resResult.getCode() == 200) {
                    SysServiceChargeHistory sysServiceChargeHistory = (SysServiceChargeHistory) resResult.getData();
                    sysFeeDeductionRecord.setStatus("成功");
                    // 操作号
                    sysFeeDeductionRecord.setOperatorNumber(sysServiceChargeHistory.getOptNo());

                    // 流水号
                    sysFeeDeductionRecord.setSerialNumber(sysServiceChargeHistory.getTraceNo());
                } else {
                    sysFeeDeductionRecord.setStatus("失败");
                    sysFeeDeductionRecord.setRemark(resResult.getMsg());
                }
                sysFeeDeductionRecord.setTransactionTime(new Date());
                sysFeeDeductionRecordMapper.insert(sysFeeDeductionRecord);
            }
        }
    }
}
