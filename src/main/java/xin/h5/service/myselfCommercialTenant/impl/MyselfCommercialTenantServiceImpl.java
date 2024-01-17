package xin.h5.service.myselfCommercialTenant.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xin.admin.domain.LoginUser;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.mapper.fundsFlowManagement.CommercialTenantOrderZFMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.common.domain.User;
import xin.h5.domain.myselfCommercialTenant.DealAndPosMachineMsg;
import xin.h5.domain.myselfCommercialTenant.query.DealAndPosMachineMsgRequestQuery;
import xin.h5.domain.myselfMachine.MerchantDetails;
import xin.h5.service.myselfCommercialTenant.MyselfCommercialTenantService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyselfCommercialTenantServiceImpl implements MyselfCommercialTenantService {

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    CommercialTenantOrderZFMapper commercialTenantOrderZFMapper;

    /**
     * 我的商户->本月交易额，累计商户中显示的列表
     *
     * @return
     */
    @Override
    public ResponseResult<DealAndPosMachineMsgRequestQuery> thisMonthDealMoney(DealAndPosMachineMsgRequestQuery query) {
        User curUser = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectByUidIsActivate(curUser.getId());
        List<DealAndPosMachineMsg> resultList = new ArrayList<>();
        for (SysPosTerminal spt : sysPosTerminalList) {
            if (spt.getMachineNo().contains(query.getDealAndPosMachineMsg().getMachineSn())) {
                DealAndPosMachineMsg dealAndPosMachineMsg = new DealAndPosMachineMsg();
                //设置sn
                dealAndPosMachineMsg.setMachineSn(spt.getMachineNo());

                //设置pos型号
                dealAndPosMachineMsg.setMachineType(spt.getClazz());

                //设置所属商户名，例如：个体户李义新
                dealAndPosMachineMsg.setMerchantName(spt.getWho());

                //设置商户号
                dealAndPosMachineMsg.setMerchantId(spt.getMerchantId());

                BigDecimal money = commercialTenantOrderZFMapper.selectBySnThisMonthMoney(spt.getMachineNo(), spt.getClazz());
                //设置本月交易额
                dealAndPosMachineMsg.setThisMonthGMV(money == null ? BigDecimal.ZERO : money);

                //设置机器的激活时间
                dealAndPosMachineMsg.setUpdateTime(spt.getUpdateTime());

                resultList.add(dealAndPosMachineMsg);
            }
        }

        // 根据 sort 属性排序 resultList
        resultList.sort((o1, o2) -> {
            switch (query.getSort()) {
                case 1: // 交易额降序
                    return o2.getThisMonthGMV().compareTo(o1.getThisMonthGMV());
                case 2: // 交易额升序
                    return o1.getThisMonthGMV().compareTo(o2.getThisMonthGMV());
                case 3: // 激活时间降序
                    return o2.getUpdateTime().compareTo(o1.getUpdateTime());
                default: // 激活时间升序
                    return o1.getUpdateTime().compareTo(o2.getUpdateTime());
            }
        });

        // 处理分页
        int startIndex = (query.getPageNumber() - 1) * query.getQuantity();
        int endIndex = Math.min(startIndex + query.getQuantity(), resultList.size());
        List<DealAndPosMachineMsg> pagedResult = resultList.subList(startIndex, endIndex);

        // 设置返回结果
        query.setResultList(pagedResult);
        query.setCount(resultList.size());

        return new ResponseResult<>(200, "查询成功！", query);
    }

    /**
     * 我的商户->本月交易额->点击后的商户详情
     * 根据商户号，查询商户详情
     *
     * @return
     */
    @Override
    public ResponseResult<MerchantDetails> queryMerchantDetails(String merchantId) {

        //把用户数据从sys_user和sys_pos_terminal，放入返中
        MerchantDetails result = sysPosTerminalMapper.queryByMerchantIdDetails(merchantId);

        SysPosTerminal sysPosTerminal = sysPosTerminalMapper.queryByMerchantIdToSysPosTerminal(merchantId);
        result.setPosMoney(commercialTenantOrderZFMapper.selectBySnThisMonthMoney(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz()));
        result.setHistoryMoney(commercialTenantOrderZFMapper.selectBySnHistoryMoney(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz()));
        result.setPosNum(commercialTenantOrderZFMapper.selectBySnThisMonthNum(sysPosTerminal.getMachineNo(), sysPosTerminal.getClazz()));
        return new ResponseResult<>(200, "查询成功！", result);
    }
}
