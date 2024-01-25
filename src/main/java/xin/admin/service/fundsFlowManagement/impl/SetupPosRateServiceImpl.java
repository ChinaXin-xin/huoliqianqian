package xin.admin.service.fundsFlowManagement.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.ServiceCharge;
import xin.admin.domain.fundsFlowManagement.SetupPosRate;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.domain.fundsFlowManagement.SysServiceChargeHistory;
import xin.admin.mapper.fundsFlowManagement.SysFeeDeductionRecordMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.mapper.fundsFlowManagement.SysServiceChargeHistoryMapper;
import xin.admin.service.fundsFlowManagement.SetupPosRateService;
import xin.zhongFu.constants.EnvAndApiConstant;
import xin.zhongFu.constants.TokenTypeConstant;
import xin.zhongFu.demo.TestGetTokenDemo;
import xin.zhongFu.model.req.merchActivity.MerchCollectQueryReq;
import xin.zhongFu.model.req.merchActivity.MerchCollectReq;
import xin.zhongFu.model.resp.BaseRespEntity;
import xin.zhongFu.model.resp.MerchCollectQueryResp;
import xin.zhongFu.model.resp.MerchCollectResp;
import xin.zhongFu.model.resp.RespCodeEnum;
import xin.zhongFu.utils.HttpRestTempUtils;
import xin.zhongFu.utils.MapUtils;
import xin.zhongFu.utils.signutil.SignUtil;

import java.util.List;
import java.util.TreeMap;

@Slf4j
@Service
public class SetupPosRateServiceImpl implements SetupPosRateService {

    @Autowired
    SysServiceChargeHistoryMapper sysServiceChargeHistoryMapper;

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    SysFeeDeductionRecordMapper sysFeeDeductionRecordMapper;

    @Override
    public ResponseResult set(SetupPosRate setupPosRate) {

        // 查询对应id的pos机
        SysPosTerminal sysPosTerminal = sysPosTerminalMapper.selectById(setupPosRate.getId());
        if (sysPosTerminal == null) {
            return new ResponseResult(400, "设置的pos机编号不存在");
        }
        if (!sysPosTerminal.getType().equals("3")) {
            return new ResponseResult(400, "设置的pos机未激活");
        }

        String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_MERCH_FEE_COLLECTION;
        MerchCollectReq merchReq = new MerchCollectReq();
        merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
        String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_TOKEN;
        String tokenInfo = null;
        try {
            tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl, EnvAndApiConstant.ENV_TEST_AGENT_ID, TokenTypeConstant.TOKEN_TYPE_2083, EnvAndApiConstant.ENV_TEST_KEY);
        } catch (IllegalAccessException e) {
            return new ResponseResult(400, "获取中付令牌错误！");
        }
        merchReq.setToken(tokenInfo);
        merchReq.setTraceNo(String.valueOf(System.currentTimeMillis()));
        merchReq.setMerchId(sysPosTerminal.getMerchantId()); // pos商户号
        merchReq.setDirectAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);


        // 查询对应sn编码的pos机
        merchReq.setSn(sysPosTerminal.getMachineNo());

        if (setupPosRate.getPosCharge() != null && !setupPosRate.getPosCharge().isEmpty()) {
            merchReq.setPosCharge(setupPosRate.getPosCharge());
        }

        if (setupPosRate.getVipCharge() != null && !setupPosRate.getVipCharge().isEmpty()) {
            merchReq.setVipCharge(setupPosRate.getVipCharge());
        }

        if (setupPosRate.getSimCharge() != null && !setupPosRate.getSimCharge().isEmpty()) {
            merchReq.setSimCharge(setupPosRate.getSimCharge());
        }


        merchReq.setSmsCode("0001");

        TreeMap<String, Object> signMap = null;
        try {
            signMap = MapUtils.objToMap(merchReq);
        } catch (IllegalAccessException e) {
            return new ResponseResult(400, "获取中付响应解码错误！");
        }

        // --------------------
        SysServiceChargeHistory sysServiceChargeHistory = new SysServiceChargeHistory();
        sysServiceChargeHistory.setSnId(setupPosRate.getId());
        sysServiceChargeHistory.setOptNo("6666666666666666");
        sysServiceChargeHistory.setTraceNo(merchReq.getTraceNo());

        return new ResponseResult(200, "设置成功！", sysServiceChargeHistory);
        // --------------------

/*        String signStr = SignUtil.signByMap(EnvAndApiConstant.ENV_TEST_KEY, signMap);
        merchReq.setSign(signStr);
        String reqJsonStr = JSON.toJSONString(merchReq);

        BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl, reqJsonStr);
        if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
            log.error("商户服务费代收,CODE:{}, MESSAGE:{}", respEntity.getCode(), respEntity.getMessage());
            return new ResponseResult(400, "设置失败," + String.format("商户服务费代收,CODE:%s, MESSAGE:%s", respEntity.getCode(), respEntity.getMessage()));
        }

        MerchCollectResp merchResp = JSON.parseObject(respEntity.getData(), MerchCollectResp.class);
        String optNo = merchResp.getOptNo();
        System.out.println("商户服务费代收返回操作序列号optNo:" + optNo);

        // 交易记录存入数据库，方便查询
        SysServiceChargeHistory sysServiceChargeHistory = new SysServiceChargeHistory();
        sysServiceChargeHistory.setSnId(setupPosRate.getId());
        sysServiceChargeHistory.setOptNo(optNo);
        sysServiceChargeHistory.setTraceNo(merchReq.getTraceNo());

        QueryWrapper<SysServiceChargeHistory> qw = new QueryWrapper<>();
        qw.eq("sn_id", sysPosTerminal.getMachineNo());

        if (sysServiceChargeHistoryMapper.update(sysServiceChargeHistory, qw) == 0) {
            sysServiceChargeHistoryMapper.insert(sysServiceChargeHistory);
        }

        // 设置成功后更新机器的流量费
        sysPosTerminal.setSimCharge(setupPosRate.getSimCharge());
        sysPosTerminalMapper.updateById(sysPosTerminal);

        return new ResponseResult(200, "设置成功！", sysServiceChargeHistory);*/
    }

    @Override
    public ResponseResult get(SetupPosRate setupPosRate) {

        // 查询对应id的pos机
        SysPosTerminal sysPosTerminal = sysPosTerminalMapper.selectById(setupPosRate.getId());

        if (sysPosTerminal == null) {
            return new ResponseResult(400, "查询的pos机编号不存在");
        }
        if (!sysPosTerminal.getType().equals("3")) {
            return new ResponseResult(400, "查询的pos机未激活");
        }

        QueryWrapper<SysServiceChargeHistory> qw = new QueryWrapper<>();
        qw.eq("sn_id", sysPosTerminal.getId());
        SysServiceChargeHistory sysServiceChargeHistory = sysServiceChargeHistoryMapper.selectOne(qw);

        if (sysServiceChargeHistory == null) {
            SetupPosRate result = new SetupPosRate();
            result.setId(setupPosRate.getId());
            return new ResponseResult(200, "查询成功！", result);
        }

        String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_MERCH_FEE_COLLECTION_QUERY;
        MerchCollectQueryReq merchReq = new MerchCollectQueryReq();
        merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
        String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_TOKEN;
        String tokenInfo = null;
        try {
            tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl, EnvAndApiConstant.ENV_TEST_AGENT_ID, TokenTypeConstant.TOKEN_TYPE_2087, EnvAndApiConstant.ENV_TEST_KEY);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        merchReq.setToken(tokenInfo);
        merchReq.setTraceNo(sysServiceChargeHistory.getTraceNo());
        merchReq.setOptNo(sysServiceChargeHistory.getOptNo());

        TreeMap<String, Object> signMap = null;
        try {
            signMap = MapUtils.objToMap(merchReq);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        String signStr = SignUtil.signByMap(EnvAndApiConstant.ENV_TEST_KEY, signMap);
        merchReq.setSign(signStr);
        String reqJsonStr = JSON.toJSONString(merchReq);

        BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl, reqJsonStr);
        if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
            log.error("商户服务费代收,CODE:{}, MESSAGE:{}", respEntity.getCode(), respEntity.getMessage());
        }
        //Data返回数据为List
        List<MerchCollectQueryResp> merchResp = JSONArray.parseArray(respEntity.getData(), MerchCollectQueryResp.class);
        System.out.println(respEntity.getData());

        // 初始化ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 使用TypeReference来指定List中的类型
            List<ServiceCharge> serviceCharges = objectMapper.readValue(
                    respEntity.getData(),
                    new TypeReference<List<ServiceCharge>>() {
                    }
            );

            if (serviceCharges == null || serviceCharges.size() == 0) {
                return new ResponseResult(400, "查询失败！");
            }
            setupPosRate.setSimCharge(String.valueOf(serviceCharges.get(0).getSimCharge()));
            setupPosRate.setPosCharge(String.valueOf(serviceCharges.get(0).getPosCharge()));
            setupPosRate.setVipCharge(String.valueOf(serviceCharges.get(0).getVipCharge()));
            return new ResponseResult(200, "查询成功！", setupPosRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseResult(400, "查询失败！");
    }
}
