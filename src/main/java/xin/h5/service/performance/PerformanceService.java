package xin.h5.service.performance;

import xin.admin.domain.fundsFlowManagement.CommercialTenantOrderZF;

public interface PerformanceService {

    /**
     * 定时任务，定时更新数据，pos机的每日交易数据
     */
    void periodicUpdate(CommercialTenantOrderZF addOrderZF);
}
