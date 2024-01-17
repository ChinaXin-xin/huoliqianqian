package xin.admin.service.other;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.other.SysPosType;

import java.util.List;

public interface SysPosTypeService {
    ResponseResult<List<SysPosType>> getAllPosType();
}
