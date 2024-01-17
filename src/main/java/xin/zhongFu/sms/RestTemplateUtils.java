package xin.zhongFu.sms;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * @author dingyu
 * @version 1.0
 * @name RestTemplateUtils
 * @date 2018/10/26 10:24
 * @description
 */
public class RestTemplateUtils {

    private static class SingletonRestTemplate {
        static final RestTemplate INSTANCE = new RestTemplate(new SimpleClientHttpRequestFactory() {{
            setConnectTimeout(10000);
            setReadTimeout(30000);
        }});
    }

    private RestTemplateUtils() {
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
    public static ResponseEntity post(String url, String data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Accpet-Encoding", "gzip");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        HttpEntity<String> formEntity = new HttpEntity<>(data, headers);
        String str = RestTemplateUtils.getInstance().postForObject(url, formEntity, String.class);
        return JSON.parseObject(str, ResponseEntity.class);
    }

    /**
     * get
     */
    public static ResponseEntity get(String url) {
        String str = RestTemplateUtils.getInstance().getForObject(url, String.class);
        return JSON.parseObject(str, ResponseEntity.class);
    }

    /**
     * get
     */
    public static ResponseEntity get(String url, Map<String, Object> parmeters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (parmeters != null && parmeters.keySet().size()>0) {
            for (String key : parmeters.keySet()) {
                builder.queryParam(key, parmeters.get(key));
            }
        }
        String str = RestTemplateUtils.getInstance().getForObject(builder.build().toUriString(), String.class);
        return JSON.parseObject(str, ResponseEntity.class);
    }

    /**
     * put修改对象
     */
    public void put(String url, String data) {
        RestTemplateUtils.getInstance().put(url, null, data);
    }

    /**
     * delete根据ID删除对象
     */
    public void delete(String url, String id) {
        RestTemplateUtils.getInstance().delete(url, id);
    }
}
