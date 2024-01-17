package xin.yunhuo.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import xin.yunhuo.base.TokenUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Huangdaye
 * @Desc 请求工具类
 * @Date 2020/7/8 16:35
 */
public class HttpUtils {

	/**
	 * @return java.lang.String
	 * @Author huangdaye
	 * @Description 发送http请求
	 * @Date 16:38 2020/7/8
	 * @Param [url, dataMap]
	 **/
	public static String apiInvoke(String url, Map<String, Object> dataMap, String token) {
		//json序列化参数
		String dataJson = JSON.toJSONString(dataMap);

		//加密，加签
		String encStr = RsaUtils.clientEncrypt(dataJson);
		String signStr = RsaUtils.clientSign(dataJson);

		//定义传输的map
		Map<String, Object> data = new HashMap<>();
		data.put("encryptStr", encStr);
		data.put("signStr", signStr);

		//System.out.println("请求参数:" + JSON.toJSONString(data));

		//获取Authorization
		String authorization = TokenUtils.getAuthorization();

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>token:" + token);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Authorization:" + authorization);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>url:" + url);

		//发送http-post(json)请求
		HttpResponse response = HttpRequest.post(url)
				.header("Authorization", authorization)
				.header("token", token)
				.header("tenant", "MDAwMA==")
				.form(data).timeout(400000).execute();
		int status = response.getStatus();
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<http响应码:" + status);
		if (status == 200) {
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<Http响应成功");
			String body = response.body();
			return body;
		} else {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxHttp响应失败");
			return null;
		}
	}

	public static String jsonApiInvoke(String url, Map<String, Object> dataMap, String token) {
		//json序列化参数
		String dataJson = JSON.toJSONString(dataMap);

		//加密，加签
		String encStr = RsaUtils.clientEncrypt(dataJson);
		String signStr = RsaUtils.clientSign(dataJson);

		//定义传输的map
		Map<String, Object> data = new HashMap<>();
		data.put("encryptStr", encStr);
		data.put("signStr", signStr);

		//System.out.println("请求参数:" + JSON.toJSONString(data));

		//获取Authorization
		String authorization = TokenUtils.getAuthorization();

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>token:" + token);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Authorization:" + authorization);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>url:" + url);

		//发送http-post(json)请求
		HttpResponse response = HttpUtil.createPost(url)
				.body(JSON.toJSONString(data))
				.header("content-type", "application/json")
                //clientId和client_secret
				.header("Authorization", authorization)
                //token,有效期8小时
				.header("token", token)
                //tenant固定值，无需替换
				.header("tenant", "SEFPU0hBTkdZSQ==")
				.timeout(20000).execute();
		int status = response.getStatus();
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<http响应码:" + status);
		if (status == 200) {
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<Http响应成功");
			String body = response.body();
			return body;
		} else {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxHttp响应失败");
			return null;
		}
	}

	public static String jsonApiInvoke(String url, Map<String, Object> dataMap, String token,String frontBase64,String backBase64) {
		//json序列化参数
		String dataJson = JSON.toJSONString(dataMap);

		//加密，加签
		String encStr = RsaUtils.clientEncrypt(dataJson);
		String signStr = RsaUtils.clientSign(dataJson);

		//定义传输的map
		Map<String, Object> data = new HashMap<>();
		data.put("encryptStr", encStr);
		data.put("signStr", signStr);
		//图片参数不加密
		data.put("fileBase64Str1", frontBase64);
		data.put("fileBase64Str2", backBase64);

		//System.out.println("请求参数:" + JSON.toJSONString(data));

		//获取Authorization
		String authorization = TokenUtils.getAuthorization();

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>token:" + token);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Authorization:" + authorization);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>url:" + url);

		//发送http-post(json)请求
		HttpResponse response = HttpUtil.createPost(url)
				.body(JSON.toJSONString(data))
				.header("content-type", "application/json")
				//clientId和client_secret
				.header("Authorization", authorization)
				//token,有效期8小时
				.header("token", token)
				//tenant固定值，无需替换
				.header("tenant", "MDAwMA==")
				.timeout(20000).execute();
		int status = response.getStatus();
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<http响应码:" + status);
		if (status == 200) {
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<Http响应成功");
			String body = response.body();
			return body;
		} else {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxHttp响应失败");
			return null;
		}
	}
	/**
	 * @return java.lang.String
	 * @Author huangdaye
	 * @Description 带文件base64的请求
	 * @Date 9:01 2020/8/11
	 * @Param [url, dataMap, token,frontBase64,backBase64]
	 **/
	public static String apiFileInvoke(String url, Map<String, Object> dataMap, String token, String frontBase64, String backBase64) {
		//json序列化参数
		String dataJson = JSON.toJSONString(dataMap);

		//加密，加签
		String encStr = RsaUtils.clientEncrypt(dataJson);
		String signStr = RsaUtils.clientSign(dataJson);

		//定义传输的map
		Map<String, Object> data = new HashMap<>();
		data.put("encryptStr", encStr);
		data.put("signStr", signStr);
		//图片参数不加密
		data.put("fileBase64Str1", frontBase64);
		data.put("fileBase64Str2", backBase64);
		data.put("tenant", "MDAwMA==");

		//System.out.println("请求参数:" + JSON.toJSONString(data));

		//获取Authorization
		String authorization = TokenUtils.getAuthorization();

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>token:" + token);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Authorization:" + authorization);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>url:" + url);

		//发送http-post(form)请求
		HttpResponse response = HttpRequest.post(url)
				.header("Authorization", authorization)
				.header("token", token)
				.header("tenant", "MDAwMA==")
				.form(data).timeout(200000).execute();
		int status = response.getStatus();
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<http响应码:" + status);
		if (status == 200) {
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<Http响应成功");
			String body = response.body();
			return body;
		} else {
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxHttp响应失败");
			return null;
		}

	}

}
