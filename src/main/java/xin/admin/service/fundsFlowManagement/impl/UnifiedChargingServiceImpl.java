package xin.admin.service.fundsFlowManagement.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.*;
import xin.admin.mapper.fundsFlowManagement.PosFeeRateMapper;
import xin.admin.mapper.fundsFlowManagement.SysFeeDeductionRecordMapper;
import xin.admin.mapper.fundsFlowManagement.SysFeeRateMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.service.fundsFlowManagement.SetupPosRateService;
import xin.admin.service.fundsFlowManagement.UnifiedChargingService;
import xin.zhongFu.constants.EnvAndApiConstant;
import xin.zhongFu.constants.TokenTypeConstant;
import xin.zhongFu.demo.TestGetTokenDemo;
import xin.zhongFu.model.req.merchActivity.MerchFeeEditReq;
import xin.zhongFu.model.req.merchActivity.MerchFeeQueryReq;
import xin.zhongFu.model.resp.BaseRespEntity;
import xin.zhongFu.model.resp.MerchFeeQueryResp;
import xin.zhongFu.model.resp.RespCodeEnum;
import xin.zhongFu.utils.HttpRestTempUtils;
import xin.zhongFu.utils.MapUtils;
import xin.zhongFu.utils.signutil.SignUtil;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

@Slf4j
@Service
public class UnifiedChargingServiceImpl implements UnifiedChargingService {

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Autowired
    SetupPosRateService setupPosRateService;

    @Autowired
    SysFeeRateMapper sysFeeRateMapper;

    @Autowired
    PosFeeRateMapper posFeeRateMapper;

    @Autowired
    SysFeeDeductionRecordMapper sysFeeDeductionRecordMapper;

    @Override
    public ResponseResult simCharging(SetupPosRate setupPosRate) {

        if (setupPosRate == null) {
            return new ResponseResult(400, "参数错误！");
        }

        if (setupPosRate.getSimCharge() == null) {
            return new ResponseResult(400, "参数错误！");
        }

        setupPosRate.setPosCharge("0");
        setupPosRate.setVipCharge("0");

        QueryWrapper<SysPosTerminal> qw = new QueryWrapper<>();
        qw.eq("type", "3");  // 查询已经激活的
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectList(qw);
        int succeed = 0;  // 一共扣款成功了多少条
        for (SysPosTerminal spt : sysPosTerminalList) {
            setupPosRate.setId(spt.getId());
            try {
                if (setupPosRateService.set(setupPosRate).getCode() == 200) {
                    succeed++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResponseResult<>(200, String.format("‘流量费’ 扣款成功：%d台，扣款失败：%d台", succeed, sysPosTerminalList.size() - succeed));
    }

    @Override
    public ResponseResult posCharging(SetupPosRate setupPosRate) {

        if (setupPosRate == null) {
            return new ResponseResult(400, "参数错误！");
        }

        if (setupPosRate.getPosCharge() == null) {
            return new ResponseResult(400, "参数错误！");
        }

        setupPosRate.setSimCharge("0");
        setupPosRate.setVipCharge("0");

        QueryWrapper<SysPosTerminal> qw = new QueryWrapper<>();
        qw.eq("type", "3");  // 查询已经激活的
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectList(qw);
        int succeed = 0;  // 一共扣款成功了多少条
        for (SysPosTerminal spt : sysPosTerminalList) {
            setupPosRate.setId(spt.getId());
            try {
                if (setupPosRateService.set(setupPosRate).getCode() == 200) {
                    succeed++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResponseResult<>(200, String.format("‘押金’ 扣款成功：%d台，扣款失败：%d台", succeed, sysPosTerminalList.size() - succeed));
    }

    @Override
    public ResponseResult vipCharging(SetupPosRate setupPosRate) {

        if (setupPosRate == null) {
            return new ResponseResult(400, "参数错误！");
        }

        if (setupPosRate.getVipCharge() == null) {
            return new ResponseResult(400, "参数错误！");
        }

        setupPosRate.setSimCharge("0");
        setupPosRate.setPosCharge("0");

        QueryWrapper<SysPosTerminal> qw = new QueryWrapper<>();
        qw.eq("type", "3");  // 查询已经激活的
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectList(qw);
        int succeed = 0;  // 一共扣款成功了多少条
        for (SysPosTerminal spt : sysPosTerminalList) {
            setupPosRate.setId(spt.getId());
            try {
                if (setupPosRateService.set(setupPosRate).getCode() == 200) {
                    succeed++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResponseResult<>(200, String.format("‘会员费’ 扣款成功：%d台，扣款失败：%d台", succeed, sysPosTerminalList.size() - succeed));
    }

    // 把MerchFeeQueryResp转为MerchFeeEditReq
    // MerchFeeQueryResp为查询无感费率的，MerchFeeEditReq为设置无感费率的
    public static MerchFeeEditReq convertToEditReq(MerchFeeQueryResp queryResp) {
        if (queryResp == null) {
            return null;
        }

        MerchFeeEditReq editReq = new MerchFeeEditReq();
        editReq.setMerchId(queryResp.getMerchId());
        editReq.setCFeeRate(queryResp.getCFeeRate());
        editReq.setDFeeRate(queryResp.getDFeeRate());
        editReq.setDFeeMax(queryResp.getDFeeMax());
        editReq.setWechatPayFeeRate(queryResp.getWechatPayFeeRate());
        editReq.setAlipayFeeRate(queryResp.getAlipayFeeRate());
        editReq.setYcFreeFeeRate(queryResp.getYcFreeFeeRate());
        editReq.setYdFreeFeeRate(queryResp.getYdFreeFeeRate());
        editReq.setD0FeeRate(queryResp.getD0FeeRate());
        editReq.setD0SingleCashDrawal(queryResp.getD0SingleCashDrawal());

        return editReq;
    }

    /**
     * 设置无感扣费，是按百分比还是按多少元
     * status：
     * true：D0手续费费率(%)
     * false：D0单笔提现(元)
     *
     * @param param
     * @return
     */
    @Override
    public ResponseResult setD0SingleCashDrawalOrD0FeeRate(MerchFeeQueryResp param, Boolean status) {

        if (param == null) {
            return new ResponseResult(400, "请求参数错误！");
        }
        if (status) {
            if (param.getD0FeeRate() == null) {
                return new ResponseResult(400, "请求参数错误！");
            }
        } else {
            if (param.getD0SingleCashDrawal() == null) {
                return new ResponseResult(400, "请求参数错误！");
            }
        }

        // 统一费率
        SysFeeRate sysFeeRate = new SysFeeRate();
        QueryWrapper<SysFeeRate> sfQw = new QueryWrapper<>();
        if (status) {
            sfQw.eq("fee_rate_name", "D0手续费费率");
            sysFeeRate.setFeeRate(param.getD0FeeRate());
        } else {
            sfQw.eq("fee_rate_name", "D0单笔提现");
            sysFeeRate.setFeeRate(param.getD0SingleCashDrawal());
        }

        // 更新为最新的无感扣费
        sysFeeRateMapper.update(sysFeeRate, sfQw);

        QueryWrapper<SysPosTerminal> qw = new QueryWrapper<>();
        qw.eq("type", "3");  // 查询已经激活的
        List<SysPosTerminal> sysPosTerminalList = sysPosTerminalMapper.selectList(qw);
        int succeed = 0;
        for (SysPosTerminal spt : sysPosTerminalList) {
            SysFeeDeductionRecord sysFeeDeductionRecord = new SysFeeDeductionRecord();

            sysFeeDeductionRecord.setTransactionTime(new Date());
            sysFeeDeductionRecord.setPos(spt.getMachineNo());

            ResponseResult<MerchFeeQueryResp> merchFeeQueryRespResponseResult = getPosRate(spt.getMerchantId());
            if (merchFeeQueryRespResponseResult.getCode() == 400) {
                sysFeeDeductionRecord.setRemark("设置失败，未查询到该机器的费率信息！");
                sysFeeDeductionRecord.setStatus("失败");
                sysFeeDeductionRecordMapper.insert(sysFeeDeductionRecord);
                continue;
            }

            // 查询原来的费率，原来的费率不变
            MerchFeeEditReq nowFeeEditReq = convertToEditReq(merchFeeQueryRespResponseResult.getData());

            if (status) {
                // D0手续费费率(%)
                PosFeeRate posFeeRate = posFeeRateMapper.selectByPosId(spt.getId(), 1);

                // 如果没有自定义的
                if (posFeeRate == null) {
                    // 设置D0手续费费率(%)
                    nowFeeEditReq.setDFeeRate(param.getD0FeeRate());

                    // 设置服务费
                    if (setPosRate(spt.getMerchantId(), convertToEditReq(param)).getCode() == 200) {
                        succeed++;
                    }
                }

                sysFeeDeductionRecord.setType("D0手续费费率(%)");
                sysFeeDeductionRecord.setAmount(param.getD0FeeRate());
            } else {

                PosFeeRate posFeeRate = posFeeRateMapper.selectByPosId(spt.getId(), 2);
                // 如果没有自定义的
                if (posFeeRate == null) {
                    // D0单笔提现(元)
                    nowFeeEditReq.setD0SingleCashDrawal(param.getD0SingleCashDrawal());

                    // 设置服务费
                    if (setPosRate(spt.getMerchantId(), convertToEditReq(param)).getCode() == 200) {
                        succeed++;
                    }
                }

                sysFeeDeductionRecord.setType("D0单笔提现(元)");
                sysFeeDeductionRecord.setAmount(param.getD0SingleCashDrawal());
            }
            sysFeeDeductionRecord.setStatus("成功");
            sysFeeDeductionRecordMapper.insert(sysFeeDeductionRecord);

        }
        if (status) {
            return new ResponseResult<>(200, String.format("‘D0手续费费率’ 设置成功：%d台，设置失败：%d台", succeed, sysPosTerminalList.size() - succeed));
        } else {
            return new ResponseResult<>(200, String.format("‘D0单笔提现(元)’ 设置成功：%d台，设置失败：%d台", succeed, sysPosTerminalList.size() - succeed));
        }
    }

    // 根据商家号，和费率，设置pos机的费率
    @Override
    public ResponseResult<MerchFeeQueryResp> setPosRate(String merchId, MerchFeeEditReq merchReq) {
/*        String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_MERCH_FEE_EDIT;

        merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
        String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_TOKEN;
        String tokenInfo = null;
        try {
            tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl, EnvAndApiConstant.ENV_TEST_AGENT_ID, TokenTypeConstant.TOKEN_TYPE_2061, EnvAndApiConstant.ENV_TEST_KEY);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        merchReq.setToken(tokenInfo);

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
            System.out.println("商户费率修改,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
        }
        System.out.println("商户费率修改返回数据:" + respEntity);
        if (respEntity.getCode().equals("00")) {
            return new ResponseResult<>(200, "设置成功！");
        } else {
            return new ResponseResult<>(400, "设置失败！");
        }*/
        Random random = new Random();
        // 随机生成true或false
        boolean isSuccess = random.nextBoolean();

        if (isSuccess) {
            return new ResponseResult<>(200, "设置成功！");
        } else {
            return new ResponseResult<>(400, "设置失败！");
        }
    }

    /**
     * 为单个pos机设置无感扣费
     *
     * @param param
     * @param status
     * @return
     */
    @Override
    public ResponseResult setAssignPosD0SingleCashDrawal(MerchFeeQueryResp param, Boolean status) {

        if (param == null) {
            return new ResponseResult(400, "请求参数错误！");
        }
        if (status) {
            if (param.getD0FeeRate() == null) {
                return new ResponseResult(400, "请求参数错误！");
            }
        } else {
            if (param.getD0SingleCashDrawal() == null) {
                return new ResponseResult(400, "请求参数错误！");
            }
        }

        SysPosTerminal spt = sysPosTerminalMapper.selectById(param.getPosId());

        if (spt == null || !spt.getType().equals("3")) {
            return new ResponseResult<>(400, "设置失败，未找到机器或者机器未激活");
        }

        ResponseResult<MerchFeeQueryResp> merchFeeQueryRespResponseResult = getPosRate(spt.getMerchantId());
        if (merchFeeQueryRespResponseResult.getCode() == 400) {
            return new ResponseResult<>(400, "设置失败，在中付未找到对应费率信息");
        }

        // 查询原来的费率，原来的费率不变
        MerchFeeEditReq nowFeeEditReq = convertToEditReq(merchFeeQueryRespResponseResult.getData());

        if (status) {
            // D0手续费费率(%)
            PosFeeRate posFeeRate = posFeeRateMapper.selectByPosId(spt.getId(), 1);

            // 如果没有自定义的
            if (posFeeRate == null) {
                // 设置D0手续费费率(%)
                nowFeeEditReq.setDFeeRate(param.getD0FeeRate());

                // 设置服务费
                setPosRate(spt.getMerchantId(), convertToEditReq(param));

                posFeeRate = new PosFeeRate();
                posFeeRate.setPosId(spt.getId().longValue());
                posFeeRate.setSysFeeRateId(1);
                // 设置当前D0手续费
                posFeeRate.setFeeRate(param.getD0FeeRate());
                posFeeRateMapper.insert(posFeeRate);
            } else {
                // 设置当前D0手续费
                posFeeRate.setFeeRate(param.getD0FeeRate());
                posFeeRateMapper.updateById(posFeeRate);
            }
        } else {

            PosFeeRate posFeeRate = posFeeRateMapper.selectByPosId(spt.getId(), 2);
            // 如果没有自定义的
            if (posFeeRate == null) {
                // D0单笔提现(元)
                nowFeeEditReq.setD0SingleCashDrawal(param.getD0SingleCashDrawal());

                // 设置服务费
                setPosRate(spt.getMerchantId(), convertToEditReq(param));

                posFeeRate = new PosFeeRate();
                posFeeRate.setPosId(spt.getId().longValue());
                posFeeRate.setSysFeeRateId(2);
                posFeeRate.setFeeRate(param.getD0SingleCashDrawal());
                posFeeRateMapper.insert(posFeeRate);
            } else {
                // D0单笔提现(元)
                posFeeRate.setFeeRate(param.getD0SingleCashDrawal());
                posFeeRateMapper.updateById(posFeeRate);
            }
        }
        return new ResponseResult<>(200, "设置成功！");
    }

    @Override
    public ResponseResult selectByPosIdToFeeRate(Integer posId) {
        if (posId == null) {
            return new ResponseResult<>(400, "参数错误！");
        }
        SysPosTerminal sysPosTerminal = sysPosTerminalMapper.selectById(posId);
        if (sysPosTerminal == null || !sysPosTerminal.getType().equals("3")) {
            return new ResponseResult<>(400, "未找到机器或者机器未激活！");
        }

        return getPosRate(sysPosTerminal.getMerchantId());
    }

    @Override
    public ResponseResult<MerchFeeQueryResp> getPosRate(String merchId) {
        String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_MERCH_FEE_QUERY;

        MerchFeeQueryReq merchReq = new MerchFeeQueryReq();
        merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
        String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_TOKEN;
        String tokenInfo = null;
        try {
            tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl, EnvAndApiConstant.ENV_TEST_AGENT_ID, TokenTypeConstant.TOKEN_TYPE_2062, EnvAndApiConstant.ENV_TEST_KEY);
        } catch (IllegalAccessException e) {
            return new ResponseResult<MerchFeeQueryResp>(400, "查询失败！");
        }

        merchReq.setToken(tokenInfo);

        // 设置商户号
        merchReq.setMerchId(merchId);

        TreeMap<String, Object> signMap = null;
        try {
            signMap = MapUtils.objToMap(merchReq);
        } catch (IllegalAccessException e) {
            return new ResponseResult<MerchFeeQueryResp>(400, "查询失败！");
        }
        String signStr = SignUtil.signByMap(EnvAndApiConstant.ENV_TEST_KEY, signMap);
        merchReq.setSign(signStr);
        String reqJsonStr = JSON.toJSONString(merchReq);

        BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl, reqJsonStr);
        if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
            System.out.println("商户费率信息查询,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
        }
        MerchFeeQueryResp merchResp = JSON.parseObject(respEntity.getData(), MerchFeeQueryResp.class);
        System.out.println("商户费率信息查询返回数据:" + merchResp);
        if (respEntity.getCode().equals("00")) {
            return new ResponseResult<MerchFeeQueryResp>(200, "查询成功！", merchResp);
        } else {
            return new ResponseResult<>(400, "查询失败！");
        }
    }
}
