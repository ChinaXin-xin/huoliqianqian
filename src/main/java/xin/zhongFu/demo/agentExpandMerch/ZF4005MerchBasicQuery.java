package xin.zhongFu.demo.agentExpandMerch;

import com.alibaba.fastjson.JSON;
import xin.zhongFu.constants.EnvAndApiConstant;
import xin.zhongFu.demo.TestGetTokenDemo;
import xin.zhongFu.model.req.AgentMerchCommonQueryReq;
import xin.zhongFu.model.req.responseDomain.ResponseDataZF;
import xin.zhongFu.model.resp.BaseRespEntity;
import xin.zhongFu.model.resp.RespCodeEnum;
import xin.zhongFu.utils.HttpRestTempUtils;
import xin.zhongFu.utils.MapUtils;
import xin.zhongFu.utils.signutil.SignUtil;

import java.util.TreeMap;

public class ZF4005MerchBasicQuery {

    /**
     * 根据商户号，查询商户信息
     * @param merchantId
     * @return
     */
    public static ResponseDataZF queryByMerchantIdToMsg(String merchantId) throws IllegalAccessException {
        String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST + "/api/acq-channel-gateway/v1/acq-channel-service/agent/expand/merch/info/query";

        AgentMerchCommonQueryReq merchReq = new AgentMerchCommonQueryReq();
        merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
        String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_TOKEN;
        String tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl, EnvAndApiConstant.ENV_TEST_AGENT_ID, "4005", EnvAndApiConstant.ENV_TEST_KEY);
        merchReq.setToken(tokenInfo);
        merchReq.setMerchId(merchantId);  //设置商户号

        TreeMap<String, Object> signMap = MapUtils.objToMap(merchReq);
        String signStr = SignUtil.signByMap(EnvAndApiConstant.ENV_TEST_KEY, signMap);
        merchReq.setSign(signStr);
        String reqJsonStr = JSON.toJSONString(merchReq);

        BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl, reqJsonStr);
        ResponseDataZF responseDataZF = JSON.parseObject(respEntity.getData(), ResponseDataZF.class);
        if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
            System.out.println("拓展商户基本信息查询,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
        }
        return responseDataZF;
    }
}
