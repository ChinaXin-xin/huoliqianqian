package xin.admin.service.fundsFlowManagement.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysFeeDeductionRecordServiceImpl extends ServiceImpl<SysFeeDeductionRecordMapper, SysFeeDeductionRecord> implements SysFeeDeductionRecordService {

    @Autowired
    SysPosTerminalMapper sysPosTerminalMapper;

    @Override
    public ResponseResult<CommonalityQuery<SysFeeDeductionRecord>> selectList(CommonalityQuery<SysFeeDeductionRecord> query) {

        Map<String, MerchActivityQueryResp> map = new HashMap<>();

        // 进行查询

        List<SysFeeDeductionRecord> allList = this.list();

        // 对allList按照id进行降序排序
        allList = allList.stream()
                .sorted(Comparator.comparing(SysFeeDeductionRecord::getId).reversed())
                .collect(Collectors.toList());

        // 查询是否缴费的所有
        for (SysFeeDeductionRecord resRecord : allList) {

            QueryWrapper<SysPosTerminal> sptQw = new QueryWrapper<>();
            sptQw.eq("machine_no", resRecord.getPos());
            SysPosTerminal spt = sysPosTerminalMapper.selectOne(sptQw);
            if (spt == null) {
                continue;
            }

            resRecord.setClazz(spt.getClazz());
            resRecord.setMerchantName(spt.getMerchantName());
            if (resRecord.getType().equals("流量")) {
                resRecord.setCoding("1");
            } else if (resRecord.getType().equals("押金")) {
                resRecord.setCoding("2");
            } else if (resRecord.getType().equals("会员")) {
                resRecord.setCoding("3");
            } else if (resRecord.getType().equals("D0单笔提现(元)")) {
                resRecord.setCoding("4");
            } else if (resRecord.getType().equals("D0手续费费率(%)")) {
                resRecord.setCoding("5");
            } else {
                resRecord.setCoding("-1");
            }

            // 如果不存在这个用户的缴费记录
            if (!map.containsKey(spt.getMerchantId())) {
                map.put(spt.getMerchantId(), queryPayOrNot(spt));
            }

            // 流量费，会员费，押金，只有这三个才有流水号能查询
            if (!(resRecord.getCoding().equals("1") || resRecord.getCoding().equals("2") || resRecord.getCoding().equals("3"))) {
                continue;
            }

            // 如果已经缴费就不查询
            if (resRecord.getPayOrNot() != null && resRecord.getPayOrNot()) {
                continue;
            } else {
                resRecord.setPayOrNot(false);
            }

            // 判断是否缴费
            MerchActivityQueryResp merchActivityQueryResp = map.get(spt.getMerchantId());

            for (MerchActivityAmtResp maa : merchActivityQueryResp.getAmtList()) {
                // 判断流水号与操作号是否相同
                System.out.println(resRecord.getId() + "   " + resRecord.getSerialNumber() + "  " + resRecord.getOperatorNumber());
                if (resRecord.getSerialNumber() == null || resRecord.getOperatorNumber() == null) {
                    continue;
                }
                if (resRecord.getSerialNumber().equals(maa.getTraceNo())
                        && resRecord.getOperatorNumber().equals(maa.getOptNo())) {
                    if (maa.getMerchPayStatus().equals("1")) {
                        resRecord.setPayOrNot(true);
                        this.baseMapper.updateById(resRecord);
                    }
                }
            }
        }


        // 初始化结果列表
        List<SysFeeDeductionRecord> resultList = new ArrayList<>();
        SysFeeDeductionRecord q = query.getQuery();

        // 只有当查询对象q不为空时，才进行过滤
        if (q != null) {
            for (SysFeeDeductionRecord resRecord : allList) {
                // 如果q中的merchantName为空字符串，则视为null
                String qMerchantName = q.getMerchantName();
                if (qMerchantName != null && qMerchantName.isEmpty()) {
                    qMerchantName = null;
                }

                // 检查merchantName是否符合条件
                boolean isMerchantNameMatch = (qMerchantName == null ||
                        (resRecord.getMerchantName() != null && resRecord.getMerchantName().contains(qMerchantName)));

                // 对其他字段执行类似处理
                String qPos = q.getPos();
                if (qPos != null && qPos.isEmpty()) {
                    qPos = null;
                }
                boolean isPosMatch = (qPos == null ||
                        resRecord.getPos().contains(qPos));

                Boolean qPayOrNot = q.getPayOrNot();
                if (qPayOrNot != null && qPayOrNot.toString().isEmpty()) {
                    qPayOrNot = null;
                }
                boolean isPayOrNotMatch = (qPayOrNot == null ||
                        qPayOrNot.equals(resRecord.getPayOrNot()));

                String qCoding = q.getCoding();
                if (qCoding != null && qCoding.isEmpty()) {
                    qCoding = null;
                }
                boolean isCodingMatch = (qCoding == null ||
                        qCoding.equals(resRecord.getCoding()));

                String qStatus = q.getStatus();
                if (qStatus != null && qStatus.isEmpty()) {
                    qStatus = null;
                }
                boolean isStatusMatch = (qStatus == null ||
                        qStatus.equals(resRecord.getStatus()));

                // 处理日期字段
                boolean isTransactionTimeMatch = (q.getStartTransactionTime() == null || q.getEndTransactionTime() == null ||
                        (!resRecord.getTransactionTime().before(q.getStartTransactionTime()) &&
                                !resRecord.getTransactionTime().after(q.getEndTransactionTime())));

                // 如果所有条件都匹配，将此记录添加到结果列表中
                if (isMerchantNameMatch && isPosMatch && isPayOrNotMatch && isCodingMatch && isStatusMatch && isTransactionTimeMatch) {
                    resultList.add(resRecord);
                }
            }
        }


        int pageNumber = query.getPageNumber(); // 当前页码
        int quantity = query.getQuantity(); // 每页显示的记录数
        int totalSize = resultList.size(); // 总记录数

        // 计算最大页数
        int maxPageNumber = (totalSize + quantity - 1) / quantity;

        // 检查请求的页码是否超过最大页数
        if (pageNumber > maxPageNumber) {
            // 如果超过了，返回空的结果集
            query.setResultList(new ArrayList<>());
        } else {
            // 计算当前页的起始索引和结束索引
            int startIndex = (pageNumber - 1) * quantity;
            int endIndex = Math.min(startIndex + quantity, totalSize);

            // 获取当前页的子列表
            List<SysFeeDeductionRecord> pagedresultList = resultList.subList(startIndex, endIndex);

            // 设置分页后的结果和数量
            query.setResultList(pagedresultList);
        }

        query.setCount((long) resultList.size());
        return new ResponseResult(200, "查询成功！", query);
    }


    /**
     * 用于查询历史缴费记录
     *
     * @param spt
     * @return
     */
    public static MerchActivityQueryResp queryPayOrNot(SysPosTerminal spt) {
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
