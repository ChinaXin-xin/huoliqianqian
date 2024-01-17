package xin.zhongFu.demo;

import com.alibaba.fastjson.JSON;
import xin.zhongFu.constants.EnvAndApiConstant;
import xin.zhongFu.constants.TokenTypeConstant;
import xin.zhongFu.model.req.TokenReq;
import xin.zhongFu.model.resp.BaseRespEntity;
import xin.zhongFu.model.resp.RespCodeEnum;
import xin.zhongFu.model.resp.TokenResp;
import xin.zhongFu.utils.HttpRestTempUtils;
import xin.zhongFu.utils.MapUtils;
import xin.zhongFu.utils.signutil.SignUtil;


import java.util.TreeMap;

public class TestGetTokenDemo {

	public static void main(String[] args) throws IllegalAccessException {
		String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST+EnvAndApiConstant.API_TOKEN;
		TokenReq tokenReq = new TokenReq();
		tokenReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
		tokenReq.setTokenType(TokenTypeConstant.TOKEN_TYPE_2083);
		TreeMap<String, Object> signMap = MapUtils.objToMap(tokenReq);
		String signStr = SignUtil.signByMap(EnvAndApiConstant.ENV_TEST_KEY,signMap);
		tokenReq.setSign(signStr);
		String reqJsonStr = JSON.toJSONString(tokenReq);
		BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl,reqJsonStr);
		if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
			System.out.println("获取令牌失败,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
		}
		TokenResp tokenResp = JSON.parseObject(respEntity.getData(),TokenResp.class);
		String token = tokenResp.getToken();
		System.out.println("获取令牌信息结果token:" + token);
	}
	
	public static String getToken(String reqUrl, String agentId, String tokenType, String signKey) throws IllegalAccessException {
		String tokenStr = "";
		TokenReq tokenReq = new TokenReq();
		tokenReq.setAgentId(agentId);
		tokenReq.setTokenType(tokenType);
		TreeMap<String, Object> signMap = MapUtils.objToMap(tokenReq);
		String signStr = SignUtil.signByMap(signKey,signMap);
		tokenReq.setSign(signStr);
		String reqJsonStr = JSON.toJSONString(tokenReq);
		BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl,reqJsonStr);
		if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
			System.out.println("获取令牌失败,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
		}
		TokenResp tokenResp = JSON.parseObject(respEntity.getData(),TokenResp.class);
		tokenStr = tokenResp.getToken();
		System.out.println("获取令牌信息结果token:" + tokenStr);
		return tokenStr;
	}
	
}
