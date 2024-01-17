package xin.zhongFu.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import xin.zhongFu.model.resp.BaseRespEntity;

import java.util.Map;

public class HttpRestTempUtils {

    private static class SingletonRestTemplate {
        static final RestTemplate INSTANCE = new RestTemplate(new SimpleClientHttpRequestFactory() {{
            setConnectTimeout(30000);
            setReadTimeout(120000);
        }});
    }

    private HttpRestTempUtils() {
    }

    public static RestTemplate getInstance() {
        return SingletonRestTemplate.INSTANCE;
    }

    /**
     * Post for JSON data.
     *
     * @param url  request url
     * @param data json data
     */
    public static BaseRespEntity post(String url, String data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Accpet-Encoding", "gzip");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        HttpEntity<String> formEntity = new HttpEntity<>(data, headers);
        //System.out.println("HttpRestTempUtils请求地址:" + url);
        //System.out.println("HttpRestTempUtils请求参数:" + data);
        String str = HttpRestTempUtils.getInstance().postForObject(url, formEntity, String.class);
        //System.out.println("HttpRestTempUtils返回参数------:" + str);
        return JSON.parseObject(str, BaseRespEntity.class);
    }

    /**
     * Post for JSON data.
     *
     * @param url  request url
     * @param data json data
     */
    public static String postNotify(String url, String data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Accpet-Encoding", "gzip");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        HttpEntity<String> formEntity = new HttpEntity<>(data, headers);
        String str = HttpRestTempUtils.getInstance().postForObject(url, formEntity, String.class);
        return str;
    }

    /**
     * get
     */
    public static BaseRespEntity get(String url) {
        String str = HttpRestTempUtils.getInstance().getForObject(url, String.class);
        return JSON.parseObject(str, BaseRespEntity.class);
    }

    /**
     * get
     */
    public static BaseRespEntity get(String url, Map<String, Object> parmeters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (parmeters != null && parmeters.keySet().size() > 0) {
            for (String key : parmeters.keySet()) {
                builder.queryParam(key, parmeters.get(key));
            }
        }
        String str = HttpRestTempUtils.getInstance().getForObject(builder.build().toUriString(), String.class);
        return JSON.parseObject(str, BaseRespEntity.class);
    }

    /**
     * put修改对象
     */
    public static void put(String url, String data) {
        HttpRestTempUtils.getInstance().put(url, null, data);
    }

    /**
     * put修改对象
     */
    public static void put(String url) {
        HttpRestTempUtils.getInstance().put(url, null);
    }

    /**
     * delete根据ID删除对象
     */
    public static void delete(String url, String id) {
        HttpRestTempUtils.getInstance().delete(url, id);
    }

}
