package xin.yunhuo.base;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xin.yunhuo.config.CommonConfig;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Huangdaye
 * @Desc  获取token以及Authorization
 * @Date 2020/7/9 10:28
 */
public class TokenUtils {


    /**
     * @Author huangdaye
     * @Description 获取用户信息token
     * @Date 15:30 2020/7/9
     * @Param [username, password]
     * @return java.lang.String
     **/
    public static String getToken(String username,String password){
        //定义请求参数
        Map<String,Object> model=new HashMap<>();
        model.put("account",username);  //用户名
        model.put("grantType","password"); //认证类型，固定使用password
        model.put("password",password);  //密码
        String json = JSON.toJSONString(model);

        //发送请求
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>token请求参数:"+json);
        HttpResponse response = HttpRequest.post(CommonConfig.TOKEN_URL)
                .header("Content-Type", "application/json")
                .header("Authorization",getAuthorization())
                .header("tenant","MDAwMA==")//业务编码不可更改
                .body(json).execute();

        //响应数据
        String body = response.body();
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<token接口返回数据："+body);
        String tokenStr="";
        JSONObject jsonObject = JSONObject.parseObject(body);
        if(!Objects.isNull(jsonObject)){
            jsonObject = jsonObject.getJSONObject("data");
            if(!Objects.isNull(jsonObject)){
                Object token = jsonObject.get("token");
                tokenStr=Objects.isNull(token)?"":token.toString();
            }
        }
        return "Bearer "+tokenStr;
    }


    
    /**
     * @Author huangdaye
     * @Description 获取Authorization
     * @Date 10:57 2020/7/9
     * @Param []
     * @return java.lang.String
     **/
    public static String getAuthorization(){
        String uniqueFlag=CommonConfig.CLIENT_ID+":"+CommonConfig.CLIENT_SECRET;
        uniqueFlag="Basic "+Base64.getEncoder().encodeToString(uniqueFlag.getBytes());
        return uniqueFlag;
    }
}
