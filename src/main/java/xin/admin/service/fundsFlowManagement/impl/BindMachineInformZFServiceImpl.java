package xin.admin.service.fundsFlowManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.controller.fundsFlowManagement.push.ZFInformPush;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.BindMachineInformZF;
import xin.admin.domain.other.SysPosType;
import xin.admin.mapper.UserMapper;
import xin.admin.mapper.fundsFlowManagement.BindMachineInformZFMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.mapper.other.SysPosTypeMapper;
import xin.admin.service.fundsFlowManagement.BindMachineInformZFService;
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
                if (responseDataZF!=null){
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
        }
        return new ResponseResult<>(200, "添加成功！");
    }
}
