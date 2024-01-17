package xin.h5.service.myselfMachine;

import org.springframework.web.bind.annotation.RequestBody;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.Transfer;
import xin.common.domain.User;
import xin.h5.domain.myselfMachine.MachineLibraryMsgRequestQuery;
import xin.h5.domain.myselfMachine.MyselfMachineNum;

import java.util.List;

public interface MyselfMachineService {

    /**
     * 我的机具，机具类型与对应数量，查询自己与直属下级，
     * @return
     */
    ResponseResult<List<MyselfMachineNum>> queryMyselfMachineTypeAndNumber();


    /**
     * 根据自己指定品牌的机器，也可以根据sn,是否激活，查询
     * 未激活的查询自己
     * 已经激活的查询，自己与直属下级的
     * @return
     */
    ResponseResult<MachineLibraryMsgRequestQuery> selectByMyselfTypeMachine(MachineLibraryMsgRequestQuery query);

    /**
     * 把未激活的机器划分给自己的下级
     * @param transfer
     * @return
     */
    ResponseResult transferList(@RequestBody Transfer transfer);

    /**
     * 根据手机号，查询下级的信息
     *
     * @return
     */
    ResponseResult<User> selectByPhoneToUser(String phone);

    /**
     * 对应转移按钮
     * 把已经激活的，激活的人成为代理商之后，转给他
     * @param transfer
     * @return
     */
    ResponseResult activateTransfer(@RequestBody Transfer transfer);

}
