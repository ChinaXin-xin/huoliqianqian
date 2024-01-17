package xin.admin.service.fundsFlowManagement;

import org.springframework.web.bind.annotation.RequestBody;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.PosTransferRecord;
import xin.admin.domain.fundsFlowManagement.query.PosTransferRecordRequestQuery;

public interface PosTransferRecordService {
    ResponseResult<PosTransferRecordRequestQuery> list(@RequestBody PosTransferRecordRequestQuery query);
    ResponseResult add(@RequestBody PosTransferRecord posTransferRecord);
}
