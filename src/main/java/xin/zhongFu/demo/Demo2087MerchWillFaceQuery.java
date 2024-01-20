package xin.zhongFu.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import xin.admin.domain.fundsFlowManagement.ServiceCharge;
import xin.zhongFu.constants.EnvAndApiConstant;
import xin.zhongFu.constants.TokenTypeConstant;
import xin.zhongFu.model.req.merchActivity.MerchCollectQueryReq;
import xin.zhongFu.model.resp.BaseRespEntity;
import xin.zhongFu.model.resp.MerchCollectQueryResp;
import xin.zhongFu.model.resp.RespCodeEnum;
import xin.zhongFu.utils.HttpRestTempUtils;
import xin.zhongFu.utils.MapUtils;
import xin.zhongFu.utils.signutil.SignUtil;

import java.util.List;
import java.util.TreeMap;

/**
 * 商户服务费代收查询
 */
public class Demo2087MerchWillFaceQuery {
    public static void main(String[] args) throws IllegalAccessException {
        String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_MERCH_FEE_COLLECTION_QUERY;

        MerchCollectQueryReq merchReq = new MerchCollectQueryReq();
        merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
        String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST + EnvAndApiConstant.API_TOKEN;
        String tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl, EnvAndApiConstant.ENV_TEST_AGENT_ID, TokenTypeConstant.TOKEN_TYPE_2087, EnvAndApiConstant.ENV_TEST_KEY);
        merchReq.setToken(tokenInfo);
        merchReq.setTraceNo("1705660190617");
        merchReq.setOptNo("20240119182952-2499");

        TreeMap<String, Object> signMap = MapUtils.objToMap(merchReq);
        String signStr = SignUtil.signByMap(EnvAndApiConstant.ENV_TEST_KEY, signMap);
        merchReq.setSign(signStr);
        String reqJsonStr = JSON.toJSONString(merchReq);

        BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl, reqJsonStr);
        if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
            System.out.println("商户服务费代收查询,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}