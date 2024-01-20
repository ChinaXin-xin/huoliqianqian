package xin.zhongFu.demo;

import com.alibaba.fastjson.JSON;
import xin.zhongFu.constants.EnvAndApiConstant;
import xin.zhongFu.constants.TokenTypeConstant;
import xin.zhongFu.model.req.merchActivity.MerchCollectReq;
import xin.zhongFu.model.resp.BaseRespEntity;
import xin.zhongFu.model.resp.MerchCollectResp;
import xin.zhongFu.model.resp.RespCodeEnum;
import xin.zhongFu.utils.HttpRestTempUtils;
import xin.zhongFu.utils.MapUtils;
import xin.zhongFu.utils.signutil.SignUtil;

import java.util.TreeMap;

public class Demo2083MerchCollect {

    public static void main(String[] args) throws IllegalAccessException {
        String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_MERCH_FEE_COLLECTION;

        MerchCollectReq merchReq = new MerchCollectReq();
        merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
        String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_TOKEN;
        String tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl, EnvAndApiConstant.ENV_TEST_AGENT_ID, TokenTypeConstant.TOKEN_TYPE_2083, EnvAndApiConstant.ENV_TEST_KEY);
        merchReq.setToken(tokenInfo);
        merchReq.setTraceNo(String.valueOf(System.currentTimeMillis()));
        System.out.println("请求流水：" + merchReq.getTraceNo());
        merchReq.setMerchId("748000000124073");
        merchReq.setDirectAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
        merchReq.setSn("00005702883072010418");
        merchReq.setPosCharge("0");
        merchReq.setVipCharge("0");
        merchReq.setSimCharge("1");
        merchReq.setSmsSend("1");
        merchReq.setSmsCode("0001");

        TreeMap<String, Object> signMap = MapUtils.objToMap(merchReq);
        String signStr = SignUtil.signByMap(EnvAndApiConstant.ENV_TEST_KEY, signMap);
        merchReq.setSign(signStr);
        String reqJsonStr = JSON.toJSONString(merchReq);

        BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl, reqJsonStr);
        if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
            System.out.println("商户服务费代收,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
        }

        MerchCollectResp merchResp = JSON.parseObject(respEntity.getData(), MerchCollectResp.class);
        String optNo = merchResp.getOptNo();
        System.out.println("商户服务费代收返回操作序列号optNo:" + optNo);
    }
}
