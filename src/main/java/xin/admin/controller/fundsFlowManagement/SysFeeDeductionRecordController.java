package xin.admin.controller.fundsFlowManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysFeeDeductionRecord;
import xin.admin.service.fundsFlowManagement.SysFeeDeductionRecordService;
import xin.common.domain.CommonalityQuery;

@RestController
@RequestMapping("/admin/sysFeeDeductionRecord")
public class SysFeeDeductionRecordController {
    @Autowired
    SysFeeDeductionRecordService sysFeeDeductionRecordService;

    @PostMapping("/list")
    ResponseResult<CommonalityQuery<SysFeeDeductionRecord>> list(@RequestBody CommonalityQuery<SysFeeDeductionRecord> query) {
        return sysFeeDeductionRecordService.selectList(query);
    }
}
