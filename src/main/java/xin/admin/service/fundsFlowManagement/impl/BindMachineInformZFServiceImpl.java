package xin.admin.service.fundsFlowManagement.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.controller.fundsFlowManagement.push.ZFInformPush;
import xin.admin.controller.sse.NotificationSSEController;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.*;
import xin.admin.domain.other.SysPosType;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.fundsFlowManagement.BindMachineInformZFMapper;
import xin.admin.mapper.fundsFlowManagement.SysFeeDeductionRecordMapper;
import xin.admin.mapper.fundsFlowManagement.SysFeeRateMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.mapper.other.SysPosTypeMapper;
import xin.admin.service.fundsFlowManagement.BindMachineInformZFService;
import xin.admin.service.fundsFlowManagement.SetupPosRateService;
import xin.common.domain.User;
import xin.zhongFu.demo.agentExpandMerch.ZF4005MerchBasicQuery;
import xin.zhongFu.model.req.responseDomain.ResponseDataZF;

import java.util.Date;
import java.util.List;

@Service
public class BindMachineInformZFServiceImpl implements BindMachineInformZFService {

    @Autowired
    BindMachineInformZFMapper bindMachineInformZFMapper;

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SysPosTypeMapper sysPosTypeMapper;

    @Autowired
    NotificationSSEController notificationSSEController;

    @Autowired
    SysFeeRateMapper sysFeeRateMapper;

    @Autowired
    SetupPosRateService setupPosRateService;

    @Autowired
    SysFeeDeductionRecordMapper sysFeeDeductionRecordMapper;

    @Override
    public ResponseResult add(ZFInformPush<BindMachineInformZF> zfInformPush) {
        List<BindMachineInformZF> dataList = zfInformPush.getDataList();
        for (BindMachineInformZF b : dataList) {
            //如果推送的牌子不存在就添加
            if (!sysPosTypeMapper.isTypeExist(b.getTermModel())) {
                sysPosTypeMapper.insert(new SysPosType(b.getTermModel()));
            }
            //把机器状态设置为绑定
            sysPosTerminalMapper.bindMachine(b.getTermSn(), b.getTermModel(), new Date());

            //如果重复推送就拒绝存入
            if (bindMachineInformZFMapper.existsWithTermSn(b.getTermSn(), b.getTermModel())) {
                continue;
            }

            try {
                ResponseDataZF responseDataZF = ZF4005MerchBasicQuery.queryByMerchantIdToMsg(b.getMerchantId());
                if (responseDataZF != null) {
                    String merchId = responseDataZF.getMerchId();
                    String merchName = responseDataZF.getMerchName();
                    String how = responseDataZF.getContactsName();
                    String idCard = responseDataZF.getIdCard();
                    sysPosTerminalMapper.updatePosTerminalMerchantMsg(merchName, how, merchId, b.getTermSn(), idCard);
                }

            } catch (IllegalAccessException e) {
                System.out.println("添加pos机绑定的实名信息错误");
            }

            User user = sysPosTerminalMapper.selectByMachineNoMsg(b.getTermSn(), b.getTermModel());
            user.setActivatedMachineryCount(user.getActivatedMachineryCount() + 1);
            userMapper.updateById(user);
            bindMachineInformZFMapper.insert(b);

            /*

            下面是执行押金的，因为与激活交易后第二笔扣除流量冲突，就先注释了

            QueryWrapper<SysPosTerminal> qw = new QueryWrapper<>();
            qw.eq("term_sn", b.getTermSn());
            qw.eq("term_model", b.getTermModel());
            SysPosTerminal sysPosTerminal = sysPosTerminalMapper.selectOne(qw);
            if (sysPosTerminal == null) {
                continue;
            }
            QueryWrapper<SysFeeRate> sfrQw = new QueryWrapper<>();
            sfrQw.eq("fee_rate_name", "押金费");
            SysFeeRate sysFeeRate = sysFeeRateMapper.selectOne(sfrQw);
            String money = sysFeeRate.getFeeRate();

            SysFeeDeductionRecord sysFeeDeductionRecord = new SysFeeDeductionRecord();
            sysFeeDeductionRecord.setType("押金");
            sysFeeDeductionRecord.setAmount(money);

            SetupPosRate setupPosRate = new SetupPosRate();
            setupPosRate.setId(sysPosTerminal.getId());
            setupPosRate.setSimCharge("0");
            setupPosRate.setPosCharge(money);
            setupPosRate.setVipCharge("0");

            ResponseResult resResult = setupPosRateService.set(setupPosRate);

            if (resResult.getCode() == 200) {
                SysServiceChargeHistory sysServiceChargeHistory = (SysServiceChargeHistory) resResult.getData();
                sysFeeDeductionRecord.setStatus("成功");
                // 操作号
                sysFeeDeductionRecord.setOperatorNumber(sysServiceChargeHistory.getOptNo());

                // 流水号
                sysFeeDeductionRecord.setSerialNumber(sysServiceChargeHistory.getTraceNo());
            } else {
                sysFeeDeductionRecord.setStatus("失败");
                sysFeeDeductionRecord.setRemark(resResult.getMsg());
            }
            sysFeeDeductionRecord.setTransactionTime(new Date());
            sysFeeDeductionRecordMapper.insert(sysFeeDeductionRecord);*/

            // 更新
            NotificationSSEController.data.setActiveMachinesCount(NotificationSSEController.data.getActiveMachinesCount() + 1);
            NotificationSSEController.data.setInactiveMachinesCount(NotificationSSEController.data.getInactiveMachinesCount() - 1);
            notificationSSEController.sendNotification();
        }
        return new ResponseResult<>(200, "添加成功！");
    }
}
