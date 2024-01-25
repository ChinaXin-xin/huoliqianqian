package xin.zhongFu.demo;

import com.alibaba.fastjson.JSON;
import xin.admin.domain.ResponseResult;
import xin.zhongFu.constants.EnvAndApiConstant;
import xin.zhongFu.constants.TokenTypeConstant;
import xin.zhongFu.model.req.merchActivity.MerchFeeEditReq;
import xin.zhongFu.model.resp.BaseRespEntity;
import xin.zhongFu.model.resp.RespCodeEnum;
import xin.zhongFu.utils.HttpRestTempUtils;
import xin.zhongFu.utils.MapUtils;
import xin.zhongFu.utils.signutil.SignUtil;


import java.util.TreeMap;

public class Demo2061MerchFeeEdit {

	public static void main(String[] args) throws IllegalAccessException {
		String reqUrl = EnvAndApiConstant.ENV_ADDR_TEST+EnvAndApiConstant.API_MERCH_FEE_EDIT;
		
		MerchFeeEditReq merchReq = new MerchFeeEditReq();
		merchReq.setAgentId(EnvAndApiConstant.ENV_TEST_AGENT_ID);
		String tokeReqUrl = EnvAndApiConstant.ENV_ADDR_TEST+EnvAndApiConstant.API_TOKEN;
		String tokenInfo = TestGetTokenDemo.getToken(tokeReqUrl,EnvAndApiConstant.ENV_TEST_AGENT_ID, TokenTypeConstant.TOKEN_TYPE_2061,EnvAndApiConstant.ENV_TEST_KEY);
		merchReq.setToken(tokenInfo);
		merchReq.setMerchId("748000000124073");
		merchReq.setCFeeRate("0.7");
		merchReq.setDFeeRate("0.8");
		merchReq.setDFeeMax("50");
		merchReq.setWechatPayFeeRate("0.56");
		merchReq.setAlipayFeeRate("0.57");
		merchReq.setYcFreeFeeRate("0.58");
		merchReq.setYdFreeFeeRate("0.59");
		merchReq.setD0FeeRate("0.1");
		merchReq.setD0SingleCashDrawal("0.5");
		
		TreeMap<String, Object> signMap = MapUtils.objToMap(merchReq);
		String signStr = SignUtil.signByMap(EnvAndApiConstant.ENV_TEST_KEY,signMap);
		merchReq.setSign(signStr);
		String reqJsonStr = JSON.toJSONString(merchReq);
		
		BaseRespEntity respEntity = HttpRestTempUtils.post(reqUrl,reqJsonStr);
		if (!RespCodeEnum.SERVICE_OK.getCode().equals(respEntity.getCode())) {
			System.out.println("商户费率修改,CODE:" + respEntity.getCode() + ",MESSAGE:" + respEntity.getMessage());
		}

		if (respEntity.getCode().equals("00")){
			System.out.println("成功！");
		}else{
			System.out.println("失败！");
		}
		System.out.println("商户费率修改返回数据:" + respEntity);
	}
}
