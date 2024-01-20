package xin.admin.service.serviceCharge;


import org.springframework.web.bind.annotation.RequestBody;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.fundsFlowManagement.query.SysPosTerminalRequestQuery;

import xin.admin.domain.serviceCharge.SysPosDealServiceCharge;
import xin.admin.domain.serviceCharge.SysSomePosDealServiceCharge;

import java.util.List;

public interface SysSomePosDealServiceChargeService {

    /**
     * 查询记录
     *
     * @return 查询操作的结果
     */
    ResponseResult<SysPosTerminalRequestQuery> selectSysSomePosDealServiceCharge(SysPosTerminalRequestQuery query);

    ResponseResult<SysPosTerminalRequestQuery> setIsWhiteListStatus(SysPosTerminal spt);

    ResponseResult<List<SysPosDealServiceCharge>> selectBySptId(Integer id);

    ResponseResult setServiceCharge(List<SysSomePosDealServiceCharge> list);

    ResponseResult selectBySomeServiceCharge(Integer id);
}
