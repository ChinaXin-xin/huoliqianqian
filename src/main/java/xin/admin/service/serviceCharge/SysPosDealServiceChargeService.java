package xin.admin.service.serviceCharge;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.serviceCharge.SysPosDealServiceCharge;

import java.util.List;

public interface SysPosDealServiceChargeService {

    /**
     * 添加收费区间
     * @param record
     * @return
     */
    ResponseResult<Void> addSysPosDealServiceCharge(SysPosDealServiceCharge record);

    /**
     * 删除收费区间
     * @return
     */
    ResponseResult<Void> deleteSysPosDealServiceCharge(Integer id);

    /**
     * 查询某一条收费区间
     * @return
     */
    ResponseResult<SysPosDealServiceCharge> selectByIdSysPosDealServiceCharge(Integer id);

    /**
     * 更新某一条收费区间
     * @return
     */
    ResponseResult<Void> updateSysPosDealServiceCharge(SysPosDealServiceCharge record);

    /**
     * 查询所有服务费收费区间
     * @return
     */
    ResponseResult<List<SysPosDealServiceCharge>> selectSysPosDealServiceCharge();
}
