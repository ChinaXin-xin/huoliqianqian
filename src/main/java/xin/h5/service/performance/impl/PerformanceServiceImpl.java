package xin.h5.service.performance.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.fundsFlowManagement.CommercialTenantOrderZF;
import xin.h5.domain.performance.Performance;
import xin.h5.mapper.performance.PerformanceMapper;
import xin.h5.service.performance.PerformanceService;

import java.util.Date;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    @Autowired
    PerformanceMapper performanceMapper;

    /**
     * 定时任务，定时更新数据，pos机的每日交易数据
     */

    @Override
    public void periodicUpdate(CommercialTenantOrderZF addOrderZF) {

        Performance isUpdate = performanceMapper.selectBySnToSomeDayPerformance(addOrderZF.getTermSn(), addOrderZF.getTermModel(), new Date());

        // 判断他今天是否有交易数据，没有的话就插入，有的话就更新
        if (isUpdate == null) {
            Performance performance = new Performance();
            performance.setCreateTime(new Date());
            performance.setTermSn(addOrderZF.getTermSn());
            performance.setTermModel(addOrderZF.getTermModel());
            performance.setTodayMoney(addOrderZF.getAmount());
            performanceMapper.insert(performance);
            return;
        } else {
            isUpdate.setTodayMoney(isUpdate.getTodayMoney().add(addOrderZF.getAmount()));
            performanceMapper.updateById(isUpdate);
        }
    }
}
