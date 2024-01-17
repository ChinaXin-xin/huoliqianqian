package xin.admin.service.fundsFlowManagement.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.PosTransferRecord;
import xin.admin.domain.fundsFlowManagement.query.PosTransferRecordRequestQuery;
import xin.admin.mapper.fundsFlowManagement.PosTransferRecordMapper;
import xin.admin.service.fundsFlowManagement.PosTransferRecordService;

@Service
public class PosTransferRecordServiceImpl implements PosTransferRecordService {

    @Autowired
    PosTransferRecordMapper posTransferRecordMapper;

    @Override
    public ResponseResult<PosTransferRecordRequestQuery> list(PosTransferRecordRequestQuery query) {
        Page<PosTransferRecord> page = new Page<>(query.getPageNumber(), query.getQuantity());
        Page<PosTransferRecord> pageList = posTransferRecordMapper.selectPosTransferRecord(page, query.getPosTransferRecord());
        query.setResultList(pageList.getRecords());
        query.setCount(posTransferRecordMapper.selectPosTransferRecordCount(query.getPosTransferRecord()));
        return new ResponseResult<>(200, "查询成功！", query);
    }

    @Override
    public ResponseResult add(PosTransferRecord posTransferRecord) {
        posTransferRecordMapper.insert(posTransferRecord);
        return new ResponseResult<>(200, "添加成功！");
    }
}
