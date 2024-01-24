package xin.admin.service.fundsFlowManagement;

import com.baomidou.mybatisplus.extension.service.IService;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysFeeDeductionRecord;
import xin.common.domain.CommonalityQuery;

public interface SysFeeDeductionRecordService extends IService<SysFeeDeductionRecord> {
    ResponseResult<CommonalityQuery<SysFeeDeductionRecord>> selectList(CommonalityQuery<SysFeeDeductionRecord> query);
}
