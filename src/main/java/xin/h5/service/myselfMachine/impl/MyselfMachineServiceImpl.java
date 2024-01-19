package xin.h5.service.myselfMachine.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.PosTransferRecord;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.fundsFlowManagement.Transfer;
import xin.admin.domain.membershipManagement.UserAuthenticationInfo;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.mapper.membershipManagement.UserAuthenticationInfoMapper;
import xin.admin.service.fundsFlowManagement.PosTransferRecordService;
import xin.common.domain.User;
import xin.h5.domain.myselfMachine.MachineLibraryMsgRequestQuery;
import xin.h5.domain.myselfMachine.MachineSnBelongUser;
import xin.h5.domain.myselfMachine.MyselfMachineNum;
import xin.h5.service.myselfMachine.MyselfMachineService;
import xin.level.service.UserGradationService;

import java.util.*;

@Service
public class MyselfMachineServiceImpl implements MyselfMachineService {

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserGradationService userGradationService;

    @Autowired
    PosTransferRecordService posTransferRecordService;

    @Autowired
    UserAuthenticationInfoMapper userAuthenticationInfoMapper;

    public List<MyselfMachineNum> aggregateByType(List<MyselfMachineNum> machineList) {
        Map<String, Integer> typeCounts = new HashMap<>();

        // 遍历列表，聚合数量
        for (MyselfMachineNum machine : machineList) {
            typeCounts.put(machine.getType(), typeCounts.getOrDefault(machine.getType(), 0) + machine.getNumber());
        }

        // 将聚合结果转换为 MyselfMachineNum 列表
        List<MyselfMachineNum> aggregatedList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : typeCounts.entrySet()) {
            MyselfMachineNum newMachineNum = new MyselfMachineNum(entry.getKey(), entry.getValue());
            aggregatedList.add(newMachineNum);
        }

        return aggregatedList;
    }


    /**
     * 我的机具，机具类型与对应数量，查询自己的
     *
     * @return
     */

/*  我的机具，这里查询得自己和自己直属下级的
    public ResponseResult<List<MyselfMachineNum>> queryMyselfMachineTypeAndNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curUser = ((LoginUser) authentication.getPrincipal()).getUser();
        List<MyselfMachineNum> resultList = new ArrayList<>();
        //获取自己的直属下级
        List<String> directDescendants = userGradationService.getDirectDescendants(curUser.getUserName());
        //把自己添加进去
        directDescendants.add(curUser.getUserName());
        for (String un : directDescendants) {
            User queryUser = userMapper.selectByUserNameToUser(un);
            if (queryUser == null)
                continue;
            List<MyselfMachineNum> myselfMachineNums = sysPosTerminalMapper.queryMyselfMachineTypeAndNumber(queryUser.getId());
            resultList.addAll(myselfMachineNums);

        }
        //聚合一下
        resultList = aggregateByType(resultList);
        return new ResponseResult<>(200, "查询成功！", resultList);
    }*/
    @Override
    public ResponseResult<List<MyselfMachineNum>> queryMyselfMachineTypeAndNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curUser = ((LoginUser) authentication.getPrincipal()).getUser();
        List<MyselfMachineNum> resultList = sysPosTerminalMapper.queryMyselfMachineTypeAndNumber(curUser.getId());
        //聚合一下
        resultList = aggregateByType(resultList);
        return new ResponseResult<>(200, "查询成功！", resultList);
    }

    /**
     * 根据自己指定品牌的机器，也可以根据sn,是否激活，查询
     * 未激活的查询自己
     * 已经激活的查询，自己与直属下级的
     *
     * @return
     */
    @Override
    public ResponseResult<MachineLibraryMsgRequestQuery> selectByMyselfTypeMachine(MachineLibraryMsgRequestQuery query) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curUser = ((LoginUser) authentication.getPrincipal()).getUser();
        List<MachineSnBelongUser> resultList = new ArrayList<>();

        if (query.getSysPosTerminal().getType().equals("3")) {
            //List<String> directDescendants = userGradationService.getDirectDescendants(curUser.getUserName());
            List<String> directDescendants = new ArrayList<>();
            //把自己添加进去
            directDescendants.add(curUser.getUserName());

            for (String un : directDescendants) {
                User user = userMapper.selectByUserNameToUser(un);
                if (user == null)
                    continue;
                List<MachineSnBelongUser> tempList = sysPosTerminalMapper.selectByUidToMyselfTypeMachineNotActivate(user.getId(), query.getSysPosTerminal());

                //设置所属人名字
/*                for (MachineSnBelongUser their : tempList) {
                    their.setName(user.getName());
                }*/

                resultList.addAll(tempList);
            }
            query.setResultList(resultList);
            query.setCount((long) resultList.size());
            query.setResultList(query.getResultListPage());
            return new ResponseResult<>(200, "查询成功！", query);
        } else {
            //分页插件
            Page<MachineSnBelongUser> page = new Page<>(query.getPageNumber(), query.getQuantity());
            IPage<MachineSnBelongUser> machineSnBelongUserList = sysPosTerminalMapper.selectByUidToMyselfTypeMachineAlreadyActivate(page, curUser.getId(), query.getSysPosTerminal());
            query.setResultList(machineSnBelongUserList.getRecords());
            query.setCount(machineSnBelongUserList.getTotal());
            return new ResponseResult<>(200, "查询成功！", query);
        }
    }

    /**
     * 把未激活的机器划分给自己的下级
     *
     * @param transfer
     * @return
     */
    @Override
    public ResponseResult transferList(Transfer transfer) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curUser = ((LoginUser) authentication.getPrincipal()).getUser();

        //根据手机号查询sys_user.id，再把sys_user.id设置为sys_pos_terminal.uid
        User user = sysPosTerminalMapper.selectByPhone(transfer.getPhone());
        if (user == null) {
            return new ResponseResult(400, "转入的手机号不存在！");
        }

        //判断是否是自己的直属下级
        if (!userGradationService.isAncestor(transfer.getPhone(), curUser.getUserName())) {
            return new ResponseResult(400, String.format("%s 不是 %s 的直属下级！", transfer.getPhone(), curUser.getUserName()));
        }

        String[] strings = transfer.getTransferList(); //sn吗的集合
        int count = 0;
        for (String sn : strings) {
            if (sysPosTerminalMapper.selectByMachineNo(sn) >= 1) {
                try {
                    Date curDate = new Date();

                    //接收人信息
                    User recipientPerson = sysPosTerminalMapper.selectByPhone(transfer.getPhone());

                    //根據sn查詢机具信息
                    SysPosTerminal sysPosTerminalMsg = sysPosTerminalMapper.selectByMachineNoToMachineAndClazzMsg(sn, transfer.getClazz());

                    PosTransferRecord posTransferRecord = new PosTransferRecord();
                    posTransferRecord.setTransferTime(curDate);
                    posTransferRecord.setTransferor(curUser.getPhone());  //划拨人手机号
                    posTransferRecord.setRecipient(transfer.getPhone());          //接收人手机号
                    if (posTransferRecord.getRecipient().equals(posTransferRecord.getTransferor())) {
                        System.out.println("批量划拨手机号相同！");
                        continue;
                    }
                    posTransferRecord.setSn(sn);
                    posTransferRecord.setTransferTime(sysPosTerminalMsg.getUpdateTime());
                    posTransferRecord.setDetails(String.format("【%s】收到【%s】划拨机具，SN号【%s】 型号：【%s】", recipientPerson.getName(), curUser.getName(), sn, transfer.getClazz()));
                    if (sysPosTerminalMapper.userTransfer(user.getId().intValue(), sn, transfer.getClazz(), curUser.getId()) > 0) {
                        posTransferRecordService.add(posTransferRecord);
                        count++;
                    }
                    sysPosTerminalMapper.updateDateAndClazz(sn, curDate, transfer.getClazz());
                } catch (Exception e) {
                    System.out.println("批量添加时出现错误，错误SN:" + sn);
                    e.printStackTrace();
                }
            }
        }
        return new ResponseResult(200, ("划拨成功，共" + count + "条"));
    }

    @Override
    public ResponseResult<User> selectByPhoneToUser(String phone) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curUser = ((LoginUser) authentication.getPrincipal()).getUser();
        List<String> directDescendants = userGradationService.getDirectDescendants(curUser.getUserName());
        if (directDescendants.size() == 0) {
            return new ResponseResult(400, "未查询到账户为：" + phone + " 的用户");
        }

        for (String un : directDescendants) {
            if (un.equals(phone)) {
                User user = userMapper.selectByUserNameToUser(un);
                if (user == null) {
                    return new ResponseResult(400, "未查询到账户为：" + phone + " 的用户");
                }
                return new ResponseResult(200, "查询成功！", user);
            }

        }
        return new ResponseResult(400, "未查询到账户：" + phone + " 的用户");
    }

    /**
     * 转移
     *
     * @param transfer
     * @return
     */
    @Override
    public ResponseResult activateTransfer(Transfer transfer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curUser = ((LoginUser) authentication.getPrincipal()).getUser();

        //根据手机号查询sys_user.id，再把sys_user.id设置为sys_pos_terminal.uid
        User user = sysPosTerminalMapper.selectByPhone(transfer.getPhone());
        if (user == null) {
            return new ResponseResult(400, "转入的手机号不存在！");
        }

        if (userAuthenticationInfoMapper.isAuthentication(user.getId())) {
            return new ResponseResult(400, "转入的账号未实名！");
        }

        //判断是否是自己的直属下级
        if (!userGradationService.isAncestor(transfer.getPhone(), curUser.getUserName())) {
            return new ResponseResult(400, String.format("%s 不是 %s 的直属下级！", transfer.getPhone(), curUser.getUserName()));
        }

        UserAuthenticationInfo userAuthenticationInfo = userAuthenticationInfoMapper.selectByUid(user.getId());
        SysPosTerminal sysPosTerminal = sysPosTerminalMapper.selectByMachineNoToMachineAndClazzMsg(transfer.getSn(), transfer.getClazz());

        //接收人的名字和机器激活时的名字不同，
        if (!userAuthenticationInfo.getMemberName().equals(sysPosTerminal.getWho())) {
            return new ResponseResult(400, "不能转移，接收人与机器认证人的名字不同");
        }

        String idCardNumber = userAuthenticationInfo.getIdCardNumber();
        String idCard = sysPosTerminal.getIdCard();

        // 比较前四位和后四位
        if (idCardNumber != null && idCard != null &&
                idCardNumber.length() >= 4 && idCard.length() >= 4) {
            String firstFourOfIdCardNumber = idCardNumber.substring(0, 4);
            String lastFourOfIdCardNumber = idCardNumber.substring(idCardNumber.length() - 4);
            String firstFourOfIdCard = idCard.substring(0, 4);
            String lastFourOfIdCard = idCard.substring(idCard.length() - 4);

            if (!(firstFourOfIdCardNumber.equals(firstFourOfIdCard) && lastFourOfIdCardNumber.equals(lastFourOfIdCard))) {
                return new ResponseResult(400, "转移错误，接收人与机器认证人的身份证不同");
            }
        } else {
            return new ResponseResult(400, "转移错误，接收人与机器认证人的身份证不同");
        }

        if (!sysPosTerminalMapper.selectByMachineNoAndClazzAndActivateMsg(transfer.getSn(), transfer.getClazz())) {
            return new ResponseResult(400, "该机器已经转移过一次，不能再次转移！");
        }

        int count = 0;

        //这个机器必须存在
        if (sysPosTerminalMapper.selectByMachineNo(transfer.getSn()) >= 1) {
            try {
                Date curDate = new Date();

                //接收人信息
                User recipientPerson = sysPosTerminalMapper.selectByPhone(transfer.getPhone());

                //根據sn查詢机具信息
                SysPosTerminal sysPosTerminalMsg = sysPosTerminalMapper.selectByMachineNoToMachineAndClazzMsg(transfer.getSn(), transfer.getClazz());

                PosTransferRecord posTransferRecord = new PosTransferRecord();
                posTransferRecord.setTransferTime(curDate);
                posTransferRecord.setTransferor(curUser.getPhone());  //划拨人手机号
                posTransferRecord.setRecipient(transfer.getPhone());          //接收人手机号
                if (posTransferRecord.getRecipient().equals(posTransferRecord.getTransferor())) {
                    return new ResponseResult(200, ("划拨人与接收人手机号相同"));
                }
                posTransferRecord.setSn(transfer.getSn());
                posTransferRecord.setTransferTime(sysPosTerminalMsg.getUpdateTime());
                posTransferRecord.setDetails(String.format("【%s】收到【%s】划拨机具，SN号【%s】 型号：【%s】", recipientPerson.getName(), curUser.getName(), transfer.getSn(), transfer.getClazz()));
                if (sysPosTerminalMapper.userTransfer(user.getId().intValue(), transfer.getSn(), transfer.getClazz(), curUser.getId()) > 0) {
                    posTransferRecordService.add(posTransferRecord);
                    count++;
                }
                sysPosTerminalMapper.updateDateAndClazz(transfer.getSn(), curDate, transfer.getClazz());
            } catch (Exception e) {
                System.out.println("转移时出现错误，错误SN:" + transfer.getSn());
                e.printStackTrace();
            }
        }
        return new ResponseResult(200, ("划拨成功，共" + count + "条"));
    }
}
