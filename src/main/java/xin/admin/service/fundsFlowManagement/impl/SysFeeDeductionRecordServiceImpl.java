package xin.admin.service.fundsFlowManagement.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.admin.domain.ResponseResult;
import xin.admin.domain.fundsFlowManagement.SysFeeDeductionRecord;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;
import xin.admin.mapper.fundsFlowManagement.SysFeeDeductionRecordMapper;
import xin.admin.mapper.fundsFlowManagement.SysPosTerminalMapper;
import xin.admin.service.fundsFlowManagement.SysFeeDeductionRecordService;
import xin.common.domain.CommonalityQuery;
import xin.zhongFu.constants.EnvAndApiConstant;
import xin.zhongFu.constants.TokenTypeConstant;
import xin.zhongFu.demo.TestGetTokenDemo;
import xin.zhongFu.model.req.merchActivity.MerchActivityQueryReq;
import xin.zhongFu.model.resp.BaseRespEntity;
import xin.zhongFu.model.resp.MerchActivityAmtResp;
import xin.zhongFu.model.resp.MerchActivityQueryResp;
import xin.zhongFu.model.resp.RespCodeEnum;
import xin.zhongFu.utils.HttpRestTempUtils;
import xin.zhongFu.utils.MapUtils;
import xin.zhongFu.utils.signutil.SignUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class SysFeeDeductionRecordServiceImpl extends ServiceImpl<SysFeeDeductionRecordMapper, SysFeeDeductionRecord> implements SysFeeDeductionRecordService {

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Override
    public ResponseResult<CommonalityQuery<SysFeeDeductionRecord>> selectList(CommonalityQuery<SysFeeDeductionRecord> query) {

        Map<String, MerchActivityQueryResp> map = new HashMap<>();

        // 进行查询
        List<SysFeeDeductionRecord> allList = this.list();


        // 设置所属人等信息
        for (SysFeeDeductionRecord record : allList) {
            QueryWrapper<SysPosTerminal> sptQw = new QueryWrapper<>();
            sptQw.eq("machine_no", record.getPos());
            SysPosTerminal spt = sysPosTerminalMapper.selectOne(sptQw);
            if (spt == null) {
                continue;
            }
            record.setClazz(spt.getClazz());
            record.setMerchantName(spt.getMerchantName());
            if (record.getType().equals("流量")) {
                record.setCoding("1");
            } else if (record.getType().equals("押金")) {
                record.setCoding("2");
            } else if (record.getType().equals("会员")) {
                record.setCoding("3");
            } else if (record.getType().equals("D0单笔提现(元)")) {
                record.setCoding("4");
            } else if (record.getType().equals("D0手续费费率(%)")) {
                record.setCoding("5");
            } else {
                record.setCoding("-1");
            }

            // 如果不存在这个用户的缴费记录
            if (!map.containsKey(spt.getMerchantId())) {
                map.put(spt.getMerchantId(), queryPayOrNot(spt));
            }

            record.setPayOrNot(false);

            if (!(record.getCoding().equals("1") || record.getCoding().equals("2") || record.getCoding().equals("3"))) {
                continue;
            }

            // 判断是否缴费
            MerchActivityQueryResp merchActivityQueryResp = map.get(spt.getMerchantId());

            for (MerchActivityAmtResp maa : merchActivityQueryResp.getAmtList()) {

                // 判断流水号与操作号是否相同
                if (record.getSerialNumber().equals(maa.getTraceNo())
                        && record.getOperatorNumber().equals(maa.getOptNo())) {
                    if (maa.getMerchPayStatus().equals("1")) {
                        record.setPayOrNot(true);
                    }
                }
            }
        }

        // 设置返回结果
        query.setResultList(resultList);
        query.setCount(resultPage.getTotal());

        return new ResponseResult(200, "查询成功！", query);
    }


/*
    @Override
    public ResponseResult<CommonalityQuery<SysFeeDeductionRecord>> selectList(CommonalityQuery<SysFeeDeductionRecord> query) {

        Map<String, MerchActivityQueryResp> map = new HashMap<>();

        // 创建分页对象
        Page<SysFeeDeductionRecord> page = new Page<>(query.getPageNumber(), query.getQuantity());

        // 创建查询包装器
        QueryWrapper<SysFeeDeductionRecord> qw = new QueryWrapper<>();
        qw.orderByDesc("id");
        // 检查query.getQuery()是否不为空，如果不为空，则进行条件查询
        SysFeeDeductionRecord condition = query.getQuery();
        if (condition != null) {
            // 根据字段进行模糊查询
            if (condition.getId() != null) {
                qw.eq("id", condition.getId());
            }
            if (condition.getAmount() != null) {
                qw.like("amount", condition.getAmount());
            }
            if (condition.getType() != null) {
                qw.like("type", condition.getType());
            }
            if (condition.getPos() != null) {
                qw.like("pos", condition.getPos());
            }
            if (condition.getRemark() != null) {
                qw.like("remark", condition.getRemark());
            }
            if (condition.getSerialNumber() != null) {
                qw.like("serial_number", condition.getSerialNumber());
            }
            if (condition.getOperatorNumber() != null) {
                qw.like("operator_number", condition.getOperatorNumber());
            }
            if (condition.getStatus() != null) {
                qw.like("status", condition.getStatus());
            }

            // 检查时间范围条件
            if (condition.getStartTransactionTime() != null && condition.getEndTransactionTime() != null) {
                qw.between("transaction_time", condition.getStartTransactionTime(), condition.getEndTransactionTime());
            }
        }

        // 进行查询
        IPage<SysFeeDeductionRecord> resultPage = this.page(page, qw);

        List<SysFeeDeductionRecord> resultList = resultPage.getRecords();

        // 设置所属人等信息
        for (SysFeeDeductionRecord record : resultList) {
            QueryWrapper<SysPosTerminal> sptQw = new QueryWrapper<>();
            sptQw.eq("machine_no", record.getPos());
            SysPosTerminal spt = sysPosTerminalMapper.selectOne(sptQw);
            if (spt == null) {
                continue;
            }
            record.setClazz(spt.getClazz());
            record.setMerchantName(spt.getMerchantName());
            if (record.getType().equals("流量")) {
                record.setCoding("1");
            } else if (record.getType().equals("押金")) {
                record.setCoding("2");
            } else if (record.getType().equals("会员")) {
                record.setCoding("3");
            } else if (record.getType().equals("D0单笔提现(元)")) {
                record.setCoding("4");
            } else if (record.getType().equals("D0手续费费率(%)")) {
                record.setCoding("5");
            } else {
                record.setCoding("-1");
            }

            // 如果不存在这个用户的缴费记录
            if (!map.containsKey(spt.getMerchantId())) {
                map.put(spt.getMerchantId(), queryPayOrNot(spt));
            }

            record.setPayOrNot(false);

            if (!(record.getCoding().equals("1") || record.getCoding().equals("2") || record.getCoding().equals("3"))) {
                continue;
            }

            // 判断是否缴费
            MerchActivityQueryResp merchActivityQueryResp = map.get(spt.getMerchantId());

            for (MerchActivityAmtResp maa : merchActivityQueryResp.getAmtList()) {

                // 判断流水号与操作号是否相同
                if (record.getSerialNumber().equals(maa.getTraceNo())
                        && record.getOperatorNumber().equals(maa.getOptNo())) {
                    if (maa.getMerchPayStatus().equals("1")) {
                        record.setPayOrNot(true);
                    }
                }
            }
        }

        // 设置返回结果
        query.setResultList(resultList);
        query.setCount(resultPage.getTotal());

        return new ResponseResult(200, "查询成功！", query);
    }
*/

    private MerchActivityQueryResp queryPayOrNot(SysPosTerminal spt) {
        String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_MERCH_ACTIVITY_QUERY;

        MerchActivityQueryReq merchReq = new MerchActivityQueryReq();
        merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
        String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_TOKEN;
        String tokenInfo = null;
        try {
            tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl, EnvAndApiConstant.ENV_TEST_AGENT_ID, TokenTypeConstant.TOKEN_TYPE_2085, EnvAndApiConstant.ENV_TEST_KEY);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        merchReq.setToken(tokenInfo);

        // 商户编号
        merchReq.setMerchId(spt.getMerchantId());

        // 机器sn
        merchReq.setSn(spt.getMachineNo());

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
            System.out.println("商户活动记录查询,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
        }
        MerchActivityQueryResp merchResp = JSON.parseObject(respEntity.getData(), MerchActivityQueryResp.class);
        List<MerchActivityAmtResp> merchAmtResp = merchResp.getAmtList();

        System.out.println("商户活动记录返回查询返回数据:" + merchResp);
        System.out.println("商户活动记录返回查询返回数据AMT条数:" + merchAmtResp.size());

        return merchResp;
    }
}
