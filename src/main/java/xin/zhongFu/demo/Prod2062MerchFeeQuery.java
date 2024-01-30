package xin.zhongFu.demo;

import com.alibaba.fastjson.JSON;
import xin.admin.domain.ResponseResult;
import xin.zhongFu.constants.EnvAndApiConstant;
import xin.zhongFu.constants.TokenTypeConstant;
import xin.zhongFu.model.req.merchActivity.MerchFeeQueryReq;
import xin.zhongFu.model.resp.BaseRespEntity;
import xin.zhongFu.model.resp.MerchFeeQueryResp;
import xin.zhongFu.model.resp.RespCodeEnum;
import xin.zhongFu.utils.HttpRestTempUtils;
import xin.zhongFu.utils.MapUtils;
import xin.zhongFu.utils.signutil.SignUtil;

import java.util.TreeMap;

public class Prod2062MerchFeeQuery {

	public static void main(String[] args) throws IllegalAccessException {
		String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST+EnvAndApiConstant.API_MERCH_FEE_QUERY;
		
		MerchFeeQueryReq merchReq = new MerchFeeQueryReq();
		merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
		String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST+EnvAndApiConstant.API_TOKEN;
		String tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl,EnvAndApiConstant.ENV_TEST_AGENT_ID, TokenTypeConstant.TOKEN_TYPE_2062,EnvAndApiConstant.ENV_TEST_KEY);
		merchReq.setToken(tokenInfo);
		merchReq.setMerchId("748000000123948");
		
		TreeMap<String, Object> signMap = MapUtils.objToMap(merchReq);
		String signStr = SignUtil.signByMap(EnvAndApiConstant.ENV_TEST_KEY,signMap);
		merchReq.setSign(signStr);
		String reqJsonStr = JSON.toJSONString(merchReq);
		
		BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl,reqJsonStr);
		if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
			System.out.println("商户费率信息查询,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
		}
		if (respEntity.getCode().equals("00")){
			System.out.println("成功！");
		}else{
			System.out.println("失败！");
		}

		MerchFeeQueryResp merchResp = JSON.parseObject(respEntity.getData(),MerchFeeQueryResp.class);
		System.out.println("商户费率信息查询返回数据:" + merchResp);
	}
}
