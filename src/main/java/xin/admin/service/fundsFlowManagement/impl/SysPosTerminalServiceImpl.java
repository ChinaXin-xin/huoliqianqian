package xin.admin.service.fundsFlowManagement.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.PosTransferRecord;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.fundsFlowManagement.Transfer;
import xin.admin.domain.fundsFlowManagement.add.SysPosTerminalAdd;
import xin.admin.domain.fundsFlowManagement.query.SysPosTerminalRequestQuery;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.service.fundsFlowManagement.PosTransferRecordService;
import xin.admin.service.fundsFlowManagement.SysPosTerminalService;
import xin.common.domain.User;

import java.util.Date;

@Service
public class SysPosTerminalServiceImpl implements SysPosTerminalService {

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    PosTransferRecordService posTransferRecordService;

    @Override
    public ResponseResult<SysPosTerminalRequestQuery> list(SysPosTerminalRequestQuery query) {
        query.setCount(0);
        Page<SysPosTerminal> page = new Page<>(query.getPageNumber(), query.getQuantity()); // 第1页，每页显示10条数据
        Page<SysPosTerminal> pageNode = sysPosTerminalMapper.selectSysPosTerminal(page, query.getSysPosTerminal());
        query.setResultList(pageNode.getRecords());
        query.setCount(sysPosTerminalMapper.selectSysPosTerminalCount(query.getSysPosTerminal()));
        return new ResponseResult<>(200, "查询成功！", query);
    }

    @Override
    public ResponseResult<SysPosTerminalRequestQuery> listTransferManagement(SysPosTerminalRequestQuery query) {
        query.setCount(0);
        Page<SysPosTerminal> page = new Page<>(query.getPageNumber(), query.getQuantity()); // 第1页，每页显示10条数据
        Page<SysPosTerminal> pageNode = sysPosTerminalMapper.selectSysPosTerminalTransferManagement(page, query.getSysPosTerminal());
        query.setResultList(pageNode.getRecords());
        query.setCount(sysPosTerminalMapper.selectSysPosTerminalCountTransferManagement(query.getSysPosTerminal()));
        return new ResponseResult<>(200, "查询成功！", query);
    }

    @Override
    public ResponseResult delete(SysPosTerminal sysPosTerminal) {
        // 调用Mapper方法执行删除操作
        int rowsAffected = sysPosTerminalMapper.deleteByMachineNo(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz());

        // 根据操作结果返回响应
        if (rowsAffected > 0) {
            return new ResponseResult<>(200, "删除成功！");
        } else {
            return new ResponseResult<>(400, "删除失败，未找到对应数据！");
        }
    }

    @Override
    public ResponseResult add(SysPosTerminalAdd sysPosTerminalAdd) {
        //User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        String[] strings = sysPosTerminalAdd.generateSerialNumbers();

        int count = 0;
        for (String sn : strings) {
            SysPosTerminal spt = new SysPosTerminal();
            spt.setMachineNo(sn);
            spt.setClassType(sysPosTerminalAdd.getClassType());
            spt.setUid(100);
            spt.setClazz(sysPosTerminalAdd.getClazz());
            spt.setVer(sysPosTerminalAdd.getVer());
            spt.setCreateTime(new Date());
            spt.setUpdateTime(new Date());
            if (!(sysPosTerminalMapper.selectByMachineNoCount(sn) >= 1)) {
                sysPosTerminalMapper.insert(spt);
                count++;
            }
        }
        return new ResponseResult(200, ("添加成功，共" + count + "条"));
    }

    @Override
    public ResponseResult transfer(Transfer transfer) {
        if (transfer == null || transfer.getSn() == null || transfer.getPhone() == null) {
            return new ResponseResult(400, "请求参数错误！");
        }

        if (sysPosTerminalMapper.selectByMachineNoCount(transfer.getSn()) == 0) {
            return new ResponseResult(400, "转出的sn不存在！");
        }

        User recipientUser = sysPosTerminalMapper.selectByPhone(transfer.getPhone());
        if (recipientUser == null) {
            return new ResponseResult(400, "转入的手机号不存在！");
        }
        Date curDate = new Date();

        SysPosTerminal sysPosTerminalMsg = sysPosTerminalMapper.selectByMachineNoToMachineMsg(transfer.getSn(), transfer.getClazz());

        //根据要划拨的机器，根据机器的sn，查询所属人的信息，姓名，手机号等
        User transferUserMsg = sysPosTerminalMapper.selectByMachineNoMsg(transfer.getSn(), transfer.getClazz());

        if (transferUserMsg == null) {
            return new ResponseResult(400, "机器不存在!");
        }

        PosTransferRecord posTransferRecord = new PosTransferRecord();
        posTransferRecord.setTransferTime(curDate);
        posTransferRecord.setTransferor(transferUserMsg.getPhone());  //划拨人手机号
        posTransferRecord.setRecipient(transfer.getPhone());          //接收人手机号
        if (posTransferRecord.getRecipient().equals(posTransferRecord.getTransferor())) {
            return new ResponseResult(400, "划拨失败，接收人与划拨人手机号相同！！");
        }
        posTransferRecord.setSn(transfer.getSn());
        posTransferRecord.setDetails(String.format("【%s】收到【%s】划拨机具，SN号【%s】", recipientUser.getName(), transferUserMsg.getName(), transfer.getSn()));

        if (sysPosTerminalMapper.transferIsAccordWith(recipientUser.getId().intValue(), transfer.getSn(), transfer.getClazz()) > 0) {
            posTransferRecordService.add(posTransferRecord);
            sysPosTerminalMapper.transfer(recipientUser.getId().intValue(), transfer.getSn(), transfer.getClazz());
            sysPosTerminalMapper.updateDate(transfer.getSn(), curDate, transfer.getClazz());
        } else {
            return new ResponseResult(400, "划拨失败，可能已经被划拨！");
        }
        return new ResponseResult(200, "划拨成功！");
    }

    @Override
    public ResponseResult transferList(Transfer transfer) {

        if (transfer == null || transfer.getStartSerial() == null || transfer.getEndSerial() == null || transfer.getPhone() == null) {
            return new ResponseResult(400, "请求参数错误！");
        }

        //根据手机号查询sys_user.id，再把sys_user.id设置为sys_pos_terminal.uid
        User recipientUser = sysPosTerminalMapper.selectByPhone(transfer.getPhone());
        if (recipientUser == null) {
            return new ResponseResult(400, "转如的手机号不存在！");
        }

        String[] strings = transfer.generateSerialNumbers(); //sn吗的集合
        int count = 0;
        for (String sn : strings) {
            if (sysPosTerminalMapper.selectByMachineNoCount(sn) >= 1) {
                try {
                    Date curDate = new Date();

                    //根据要划拨的机器，根据机器的sn，查询所属人的信息，姓名，手机号等
                    User transferUserMsg = sysPosTerminalMapper.selectByMachineNoMsg(sn, transfer.getClazz());

                    //根據sn查詢机具信息
                    SysPosTerminal sysPosTerminalMsg = sysPosTerminalMapper.selectByMachineNoToMachineMsg(sn, transfer.getClazz());

                    if (transferUserMsg == null || sysPosTerminalMsg == null) {
                        continue;
                    }

                    PosTransferRecord posTransferRecord = new PosTransferRecord();
                    posTransferRecord.setTransferTime(curDate);
                    posTransferRecord.setTransferor(transferUserMsg.getPhone());  //划拨人手机号
                    posTransferRecord.setRecipient(transfer.getPhone());          //接收人手机号
                    if (posTransferRecord.getRecipient().equals(posTransferRecord.getTransferor())) {
                        System.out.println("批量划拨手机号相同！");
                        continue;
                    }
                    posTransferRecord.setSn(sn);
                    posTransferRecord.setDetails(String.format("【%s】收到【%s】划拨机具，SN号【%s】", recipientUser.getName(), transferUserMsg.getName(), sn));
                    int resultCount = sysPosTerminalMapper.transferIsAccordWith(recipientUser.getId().intValue(), sn, transfer.getClazz());
                    if (resultCount > 0) {
                        posTransferRecordService.add(posTransferRecord);
                        sysPosTerminalMapper.transfer(recipientUser.getId().intValue(), sn, transfer.getClazz());
                        sysPosTerminalMapper.updateDate(sn, curDate, transfer.getClazz());
                        count++;
                    }
                } catch (Exception e) {
                    System.out.println("批量添加时出现错误，错误SN:" + sn);
                    e.printStackTrace();
                }
            }
        }
        return new ResponseResult(200, ("划拨成功，共" + count + "条"));
    }

    @Override
    public ResponseResult transferToAdmin(Transfer transfer) {
        if (transfer == null || transfer.getSn() == null) {
            return new ResponseResult(400, "请求参数错误！");
        }

        if (sysPosTerminalMapper.selectByMachineNoCount(transfer.getSn()) == 0) {
            return new ResponseResult(400, "请求参数错误，SN不存在！");
        }

        Date curDate = new Date();

        //根据要划拨的机器，根据机器的sn，查询所属人的信息，姓名，手机号等
        User transferUserMsg = sysPosTerminalMapper.selectByMachineNoMsg(transfer.getSn(), transfer.getClazz());
        SysPosTerminal sysPosTerminalMsg = sysPosTerminalMapper.selectByMachineNoToMachineMsg(transfer.getSn(), transfer.getClazz());

        transfer.setPhone("18000000000");

        //接收人信息
        User recipientPerson = sysPosTerminalMapper.selectByPhone(transfer.getPhone());

        if (transferUserMsg == null || sysPosTerminalMsg == null) {
            return new ResponseResult(400, "划拨失败，可能已经划拨！");
        }

        PosTransferRecord posTransferRecord = new PosTransferRecord();
        posTransferRecord.setTransferTime(curDate);
        posTransferRecord.setTransferor(transferUserMsg.getPhone());  //划拨人手机号
        posTransferRecord.setRecipient(transfer.getPhone());          //接收人手机号
        if (posTransferRecord.getRecipient().equals(posTransferRecord.getTransferor())) {
            return new ResponseResult(200, "转入与接收的不同！");
        }
        posTransferRecord.setSn(transfer.getSn());
        posTransferRecord.setTransferTime(sysPosTerminalMsg.getUpdateTime());
        posTransferRecord.setDetails(String.format("【%s】收到【%s】划拨机具，SN号【%s】", recipientPerson.getName(), transferUserMsg.getName(), transfer.getSn()));
        posTransferRecordService.add(posTransferRecord);

        sysPosTerminalMapper.transferAdmin(transfer.getSn());
        sysPosTerminalMapper.updateDate(transfer.getSn(), curDate, transfer.getClazz());

        return new ResponseResult(200, "回收成功！");
    }

    @Override
    public ResponseResult deleteList(SysPosTerminal[] snList) {
        int count = 0;
        for (SysPosTerminal sn : snList) {
            int rowsAffected = sysPosTerminalMapper.deleteByMachineNo(sn.getMachineNo(), sn.getClazz());
            // 根据操作结果返回响应
            if (rowsAffected > 0) {
                count++;
            }
        }
        return new ResponseResult<>(200, ("删除成功，共删除：" + count + "条"));
    }
}
