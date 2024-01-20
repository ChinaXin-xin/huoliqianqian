package xin.admin.service.serviceCharge;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.serviceCharge.SysPosDealServiceCharge;

import java.util.List;

public interface SysPosDealServiceChargeService {

    ResponseResult<Void> addSysPosDealServiceCharge(SysPosDealServiceCharge record);

    ResponseResult<Void> deleteSysPosDealServiceCharge(Integer id);

    ResponseResult<SysPosDealServiceCharge> selectByIdSysPosDealServiceCharge(Integer id);

    ResponseResult<Void> updateSysPosDealServiceCharge(SysPosDealServiceCharge record);

    ResponseResult<List<SysPosDealServiceCharge>> selectSysPosDealServiceCharge();
}
