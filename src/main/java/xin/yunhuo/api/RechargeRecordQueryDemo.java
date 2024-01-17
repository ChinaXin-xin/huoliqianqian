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
*  平台充值记录查询
* @author : chuanyin.li
* @date: 2020/9/29
*/
public class RechargeRecordQueryDemo {

    /**
     * 接口地址
     */
	private final static String URL = "http://10.18.6.22:10000/api/thirdparty/recharge/queryRecord";
    /**
     * 用户名
     */
	private final static String USERNAME = "yefei@qq.com";
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

	public static void apiInvoke() {
		Map<String, Object> model = new HashMap<>(6);
		//{"applyStatus":"04","paySerialNo":"20200911335808117651320832","platformId":"20200810324233859032858624","reqOrderNo":"20745313879052","rspCod":"120092","rspMsg":"交易失败"}
        //平台id
		model.put("merOrderNo", ""+System.currentTimeMillis());
		model.put("platformId", "20200730320279904000598016");
		model.put("startDate", "2020-07-29");
		model.put("endDate", "2020-08-03");

		//获取token ,token可以多次使用，不用频繁获取
        String token = getToken();
//		String token = "Bearer eyJ0eXAiOiJKc29uV2ViVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5rex5Zyz5biC5Lic5pa5572R6IGU5oqV6LWE5pyJ6ZmQ5YWs5Y-4LeS8gem5heW5s-WPsCIsInRva2VuX3R5cGUiOiJ0b2tlbiIsInVzZXJpZCI6IjMxMDUwMjgyMDYyODE5MzI4MCIsImFjY291bnQiOiJsaWNodWFueWluQGNyZWRsaW5rLmNvbSIsImV4cCI6MTU5NDMwOTgxNSwibmJmIjoxNTk0MjgxMDE1fQ.6bIb_E1vMynPPUtiYLk02Zd2gcoNXUS6ilfu6-kEKf4";

		//发送请求 -同打款申请不同 这里采用json格式
		String result = HttpUtils.jsonApiInvoke(URL, model, token);
		System.out.println(">>>>>>>>>>>>>>>>返回加密数据:" + result);
		JSONObject jsonObject = JSON.parseObject(result);
		System.out.println(jsonObject);
		if (jsonObject.getBoolean("isSuccess")) {
			//--------> : 解密
			String res = RsaUtils.decryptResJSONData(ObjectUtil.toString(jsonObject.get("data")));
			System.out.println("平台充值记录查询返回数据:" + res);
		}
	}

	public static void main(String[] args) {
		apiInvoke();
	}

}
