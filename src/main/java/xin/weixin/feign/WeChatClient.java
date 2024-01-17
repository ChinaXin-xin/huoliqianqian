package xin.weixin.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weChatClient", url = "https://api.weixin.qq.com")
public interface WeChatClient {

    /**
     * 获取微信的openid和session_key
     * @param appId
     * @param secret
     * @param code
     * @param grantType
     * @return
     */
    @GetMapping("/sns/jscode2session")
    String getOpenId(@RequestParam("appid") String appId,
                               @RequestParam("secret") String secret,
                               @RequestParam("js_code") String code,
                               @RequestParam("grant_type") String grantType);
}
