package xin.yunhuo.api;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xin.yunhuo.base.TokenUtils;
import xin.yunhuo.utils.HttpUtils;
import xin.yunhuo.utils.RsaUtils;

import java.util.HashMap;
import java.util.Map;

/**
*  已退汇状态订单查询
* @author : xie hong jun
* @date: 2020/9/29
*/
public class V2BackedOrderInfosDemo {

    /**
     * 接口地址
     */
	private final static String URL = "http://10.18.6.22:10000/api/thirdparty/remit/order/V2/backed/infos";
    /**
     * 用户名
     */
	private final static String USERNAME = "lichuanyin@credlink.com";
    /**
     * 密码
     */
	private final static String PASSWORD = "zuihou";

	/**
	 * @return java.lang.String
	 * @Author huangdaye
	 * @Description 获取token（此token可以多次使用，有效期为8小时，不用频繁请求token接口）
	 * @Date 15:45 2020/7/9
	 * @Param []
	 **/
	public static String getToken() {
		//获取token
		String token = TokenUtils.getToken(USERNAME, PASSWORD);
		return token;
	}

	public static void apiInvoke(){
		Map<String, Object> model = new HashMap<>(6);
		// 商户平台ID
		model.put("platformId", "20200703310502035165077504");
		// 客户请求订单号
		model.put("reqOrderNo", ""+System.currentTimeMillis());
		// 起始时间如:2020-11-11
		model.put("startDate", "2020-11-11");
		// 结束时间如:2020-11-15 (起始时间间隔不能超过三个月)
		model.put("endDate", "2020-11-15");
		// 时间查询类型: 0-订单创建时间; 1-订单完成时间
		model.put("dateType", 0);


		//获取token,token可以多次使用，不用频繁获取 有效期8小时
		String token = getToken();
//        String token="Bearer eyJ0eXAiOiJKc29uV2ViVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5rex5Zyz5biC5Lic5pa5572R6IGU5oqV6LWE5pyJ6ZmQ5YWs5Y-4LeS8gem5heW5s-WPsCIsInRva2VuX3R5cGUiOiJ0b2tlbiIsInVzZXJpZCI6IjMxMDUwMjgyMDYyODE5MzI4MCIsImFjY291bnQiOiJsaWNodWFueWluQGNyZWRsaW5rLmNvbSIsImV4cCI6MTU5NDYzNDgwMywibmJmIjoxNTk0NjA2MDAzfQ.h87SLSsjUIGXua2S1OYdoFd2-zbS2YDDNa3Zm7irE6I";

		//发送请求
		String result = HttpUtils.jsonApiInvoke(URL, model,token);
		System.out.println(">>>>>>>>>>>>>>>>返回加密数据:"+result);
		JSONObject jsonObject = JSON.parseObject(result);
		System.out.println(jsonObject);
		if(jsonObject.getBoolean("isSuccess")){
			//--------> : 解密
			String res = RsaUtils.decryptResJSONData(ObjectUtil.toString(jsonObject.get("data")));
			System.out.println("特定状态订单查询返回数据:"+res);
		}
	}

	public static void main(String[] args) {
		apiInvoke();
	}

}
