package xin.zhongFu.demo;

import com.alibaba.fastjson.JSON;
import xin.zhongFu.constants.EnvAndApiConstant;
import xin.zhongFu.constants.TokenTypeConstant;
import xin.zhongFu.model.req.merchActivity.MerchActivityQueryReq;
import xin.zhongFu.model.resp.BaseRespEntity;
import xin.zhongFu.model.resp.MerchActivityAmtResp;
import xin.zhongFu.model.resp.MerchActivityQueryResp;
import xin.zhongFu.model.resp.RespCodeEnum;
import xin.zhongFu.utils.HttpRestTempUtils;
import xin.zhongFu.utils.MapUtils;
import xin.zhongFu.utils.signutil.SignUtil;


import java.util.List;
import java.util.TreeMap;

/**
 * 商户活动记录查询
 */
public class Demo2085MerchActivityQuery {



	public static void main(String[] args) throws IllegalAccessException {
		String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST+EnvAndApiConstant.API_MERCH_ACTIVITY_QUERY;
		
		MerchActivityQueryReq merchReq = new MerchActivityQueryReq();
		merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
		String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST+EnvAndApiConstant.API_TOKEN;
		String tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl,EnvAndApiConstant.ENV_TEST_AGENT_ID, TokenTypeConstant.TOKEN_TYPE_2085,EnvAndApiConstant.ENV_TEST_KEY);
		merchReq.setToken(tokenInfo);
		merchReq.setMerchId("748000000124073");
		merchReq.setSn("00005702883072010418");
		
		TreeMap<String, Object> signMap = MapUtils.objToMap(merchReq);
		String signStr = SignUtil.signByMap(EnvAndApiConstant.ENV_TEST_KEY,signMap);
		merchReq.setSign(signStr);
		String reqJsonStr = JSON.toJSONString(merchReq);
		
		BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl,reqJsonStr);
		if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
			System.out.println("商户活动记录查询,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
		}
		MerchActivityQueryResp merchResp = JSON.parseObject(respEntity.getData(),MerchActivityQueryResp.class);
		List<MerchActivityAmtResp> merchAmtResp = merchResp.getAmtList();
		System.out.println("商户活动记录返回查询返回数据:" + merchResp);
		System.out.println("商户活动记录返回查询返回数据AMT:" + merchAmtResp.size());
	}
	
}
