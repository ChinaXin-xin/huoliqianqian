package xin.admin.service.other.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.other.SysPosType;
import xin.admin.mapper.other.SysPosTypeMapper;
import xin.admin.service.other.SysPosTypeService;

import java.util.List;

@Service
public class SysPosTypeServiceImpl implements SysPosTypeService {

    @Autowired
    SysPosTypeMapper sysPosTypeMapper;

    @Override
    public ResponseResult<List<SysPosType>> getAllPosType() {
        List<SysPosType> sysPosTypes = sysPosTypeMapper.selectList(null);
        return new ResponseResult<>(200, "查询成功！", sysPosTypes);
    }
}
