package xin.admin.controller.fundsFlowManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.query.PosTransferRecordRequestQuery;
import xin.admin.service.fundsFlowManagement.PosTransferRecordService;

@RestController
@RequestMapping("/admin/posTransferRecord")
public class PosTransferRecordController {

    @Autowired
    PosTransferRecordService service;

    @PostMapping("/list")
    public ResponseResult<PosTransferRecordRequestQuery> list(@RequestBody PosTransferRecordRequestQuery query) {
        return service.list(query);
    }
}
