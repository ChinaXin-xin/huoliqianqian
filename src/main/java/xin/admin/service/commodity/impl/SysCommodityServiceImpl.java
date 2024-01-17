package xin.admin.service.commodity.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.commodity.SysCommodity;
import xin.admin.domain.commodity.jquery.SysCommodityRequestQuery;
import xin.admin.mapper.commodity.SysCommodityMapper;
import xin.admin.service.commodity.SysCommodityService;

import java.util.List;

@Service
public class SysCommodityServiceImpl implements SysCommodityService {

    @Autowired
    SysCommodityMapper sysCommodityMapper;

    @Override
    public ResponseResult add(SysCommodity sysCommodity) {
        sysCommodityMapper.insert(sysCommodity);
        return new ResponseResult(200, "添加成功！");
    }

    @Override
    public ResponseResult<List<SysCommodity>> listAll() {
        List<SysCommodity> sysCommodities = sysCommodityMapper.selectList(null);
        return new ResponseResult<>(200, "查询成功！", sysCommodities);
    }

    @Override
    public ResponseResult<SysCommodityRequestQuery> list(SysCommodityRequestQuery query) {
        return null;
    }
}
