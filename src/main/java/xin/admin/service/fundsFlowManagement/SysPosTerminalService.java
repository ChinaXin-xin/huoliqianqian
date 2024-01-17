package xin.admin.service.fundsFlowManagement;

import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.fundsFlowManagement.Transfer;
import xin.admin.domain.fundsFlowManagement.add.SysPosTerminalAdd;
import xin.admin.domain.fundsFlowManagement.query.SysPosTerminalRequestQuery;

public interface SysPosTerminalService {
    ResponseResult<SysPosTerminalRequestQuery> list( SysPosTerminalRequestQuery query);
    ResponseResult<SysPosTerminalRequestQuery> listTransferManagement( SysPosTerminalRequestQuery query);

    ResponseResult delete(SysPosTerminal sysPosTerminal);

    ResponseResult add(SysPosTerminalAdd sysPosTerminalAdd);
    ResponseResult transfer(Transfer transfer);
    ResponseResult transferList(Transfer transfer);
    ResponseResult transferToAdmin(Transfer transfer);

    //根据sn数组批量删除SN库存信息
    public ResponseResult deleteList(SysPosTerminal[] snList);
}
