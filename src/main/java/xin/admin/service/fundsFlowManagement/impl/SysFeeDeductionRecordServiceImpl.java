package xin.admin.service.fundsFlowManagement.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysFeeDeductionRecord;
import xin.admin.mapper.fundsFlowManagement.SysFeeDeductionRecordMapper;
import xin.admin.service.fundsFlowManagement.SysFeeDeductionRecordService;
import xin.common.domain.CommonalityQuery;

@Service
public class SysFeeDeductionRecordServiceImpl extends ServiceImpl<SysFeeDeductionRecordMapper, SysFeeDeductionRecord> implements SysFeeDeductionRecordService {

    @Override
    public ResponseResult<CommonalityQuery<SysFeeDeductionRecord>> selectList(CommonalityQuery<SysFeeDeductionRecord> query) {
        // 创建分页对象
        Page<SysFeeDeductionRecord> page = new Page<>(query.getPageNumber(), query.getQuantity());

        // 创建查询包装器
        QueryWrapper<SysFeeDeductionRecord> qw = new QueryWrapper<>();
        qw.orderByDesc("id");
        // 检查query.getQuery()是否不为空，如果不为空，则进行条件查询
        SysFeeDeductionRecord condition = query.getQuery();
        if (condition != null) {
            // 根据字段进行模糊查询
            if (condition.getId() != null) {
                qw.eq("id", condition.getId());
            }
            if (condition.getAmount() != null) {
                qw.like("amount", condition.getAmount());
            }
            if (condition.getType() != null) {
                qw.like("type", condition.getType());
            }
            if (condition.getPos() != null) {
                qw.like("pos", condition.getPos());
            }
            if (condition.getRemark() != null) {
                qw.like("remark", condition.getRemark());
            }
            if (condition.getSerialNumber() != null) {
                qw.like("serial_number", condition.getSerialNumber());
            }
            if (condition.getOperatorNumber() != null) {
                qw.like("operator_number", condition.getOperatorNumber());
            }
            if (condition.getStatus() != null) {
                qw.like("status", condition.getStatus());
            }

            // 检查时间范围条件
            if (condition.getStartTransactionTime() != null && condition.getEndTransactionTime() != null) {
                qw.between("transaction_time", condition.getStartTransactionTime(), condition.getEndTransactionTime());
            }
        }

        // 进行查询
        IPage<SysFeeDeductionRecord> resultPage = this.page(page, qw);

        // 设置返回结果
        query.setResultList(resultPage.getRecords());
        query.setCount(resultPage.getTotal());

        return new ResponseResult(200, "查询成功！", query);
    }
}
