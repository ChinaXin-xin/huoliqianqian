package xin.admin.service.fundsFlowManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysFeeRate;
import xin.admin.mapper.fundsFlowManagement.SysFeeRateMapper;
import xin.admin.service.fundsFlowManagement.SysFeeRateService;

import java.util.List;

@Service
public class SysFeeRateServiceImpl implements SysFeeRateService {

    @Autowired
    SysFeeRateMapper sysFeeRateMapper;

    @Override
    public ResponseResult<List<SysFeeRate>> list() {
        List<SysFeeRate> sysFeeRates = sysFeeRateMapper.selectList(null);
        return new ResponseResult<>(200, "查询成功！", sysFeeRates);
    }

    @Override
    public ResponseResult setSimFeeRatePeriod(SysFeeRate simFee) {

        if (simFee == null || simFee.getFeeRate() == null || simFee.getIntervalDays() == null) {
            return new ResponseResult(400, "请求参数错误!");
        }

        SysFeeRate newValue = new SysFeeRate();
        newValue.setId(3); //3为流量费
        newValue.setFeeRate(simFee.getFeeRate());
        newValue.setIntervalDays(simFee.getIntervalDays());
        sysFeeRateMapper.updateById(newValue);
        return new ResponseResult(200, "更新成功！");
    }

    @Override
    public ResponseResult updatePosFeeRatePeriod(SysFeeRate simFee) {
        if (simFee == null || simFee.getFeeRate() == null) {
            return new ResponseResult(400, "请求参数错误!");
        }

        SysFeeRate newValue = new SysFeeRate();
        newValue.setId(4);  //4为押金
        newValue.setFeeRate(simFee.getFeeRate());
        newValue.setIntervalDays(simFee.getIntervalDays());
        sysFeeRateMapper.updateById(newValue);
        return new ResponseResult(200, "更新成功！");
    }

    @Override
    public ResponseResult updateVipFeeRatePeriod(SysFeeRate simFee) {
        if (simFee == null || simFee.getFeeRate() == null) {
            return new ResponseResult(400, "请求参数错误!");
        }

        SysFeeRate newValue = new SysFeeRate();
        newValue.setId(5);  //会员费
        newValue.setFeeRate(simFee.getFeeRate());
        newValue.setIntervalDays(simFee.getIntervalDays());
        sysFeeRateMapper.updateById(newValue);
        return new ResponseResult(200, "更新成功！");
    }
}
