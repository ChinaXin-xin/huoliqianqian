package xin.h5.controller.myselfMachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.Transfer;
import xin.common.domain.User;
import xin.h5.domain.myselfMachine.MachineLibraryMsgRequestQuery;
import xin.h5.domain.myselfMachine.MyselfMachineNum;
import xin.h5.service.myselfMachine.MyselfMachineService;

import java.util.List;

@RestController
@RequestMapping("/h5/MyselfMachine")
public class MyselfMachineController {

    @Autowired
    MyselfMachineService myselfMachineService;

    /**
     * 我的机具，机具类型与对应数量，查询自己与直属下级，
     *
     * @return
     */
    @PostMapping("/queryMyselfMachineTypeAndNumber")
    public ResponseResult<List<MyselfMachineNum>> queryMyselfMachineTypeAndNumber() {
        return myselfMachineService.queryMyselfMachineTypeAndNumber();
    }


    /**
     * 根据自己指定品牌的机器，也可以根据sn,是否激活，查询
     * 未激活的查询自己
     * 已经激活的查询，自己与直属下级的
     *
     * @return
     */
    @PostMapping("/selectByMyselfTypeMachine")
    public ResponseResult<MachineLibraryMsgRequestQuery> selectByMyselfTypeMachine(@RequestBody MachineLibraryMsgRequestQuery query) {
        return myselfMachineService.selectByMyselfTypeMachine(query);
    }


    /**
     * 把未激活的机器划分给自己的下级
     *
     * @param transfer
     * @return
     */
    @PostMapping("/transferList")
    public ResponseResult transferList(@RequestBody Transfer transfer) {
        return myselfMachineService.transferList(transfer);
    }

    /**
     * 根据手机号，查询下级的信息
     *
     * @return
     */
    @PostMapping("/selectByPhoneToUser")
    public  ResponseResult<User> selectByPhoneToUser(@RequestBody String phone) {
        return myselfMachineService.selectByPhoneToUser(phone);
    }

    /**
     * 对应转移按钮
     * 把已经激活的，激活的人成为代理商之后，转给他
     * @param transfer
     * @return
     */
    @PostMapping("/activateTransfer")
    public ResponseResult activateTransfer(@RequestBody Transfer transfer) {
        return myselfMachineService.activateTransfer(transfer);
    }
}
