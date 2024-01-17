package xin.zhongFu.utils;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HuToolUtil {
	
	// url:链接地址，params：填充在url中的参数
	public static String httpGet(String url, String params) {
		String requestUrl = url;
		if (StringUtils.isNotBlank(params)) {
			requestUrl = url + "?" + params;
		}
		String respData = null;
		log.info("httpGet req is 【{}】", params);
	 	HttpRequest httpRequest = HttpRequest.get(requestUrl).timeout(50000).header("token", "application/json");
		respData = httpRequest.execute().body();
		log.info(String.format("HttpsUtil:httpGet | 请求信息：%s | 响应信息: %s", httpRequest.getUrl(), respData));
		return respData;
	}
	
	// url:链接地址，params：填充在url中的参数， sendBodyData：body
	public static String httpPost(String url, String params, String sendBodyData) {
		String requestUrl = url;
	  	if (StringUtils.isNotBlank(params)) {
	  		requestUrl = url + "?" + params;
	   	}
		String respData = null;
		log.info("httpPost req is 【{}】", sendBodyData);
	  	HttpRequest httpRequest = HttpRequest.post(requestUrl).timeout(50000).header("Content-Type", "application/json");
	  	if (StringUtils.isNotBlank(sendBodyData)) {
	  		httpRequest.body(sendBodyData);
	  	}
	 	respData = httpRequest.execute().body();
	 	log.info(String.format("HttpsUtil:httpPost | 请求信息：%s | 响应信息: %s", httpRequest.getUrl(), respData));
		return respData;
	}
	
}
