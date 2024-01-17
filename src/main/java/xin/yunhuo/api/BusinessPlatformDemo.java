package xin.yunhuo.api;

import xin.yunhuo.base.TokenUtils;
import xin.yunhuo.utils.HttpUtils;
import xin.yunhuo.utils.RsaUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Huangdaye
 * @Desc 平台信息调用
 * @Date 2020/7/8 15:32
 */
public class BusinessPlatformDemo {

    private final static String URL="http://123.58.32.120:9017/api/thirdparty/platform/query";  //接口地址
    private final static String USERNAME="chifujituan@163.com";  //用户名
    private final static String PASSWORD="yc41375405"; //密码

    /**
     * @Author huangdaye
     * @Description 获取token（此token可以多次使用，有效期为8小时，不用频繁请求token接口）
     * @Date 15:45 2020/7/9
     * @Param []
     * @return java.lang.String
     **/
    public static String getToken(){
        //获取token
        String token = TokenUtils.getToken(USERNAME,PASSWORD);
        return token;
    }

    public static void apiInvoke(){
        //定义请求参数
        Map<String,Object> model=new HashMap<>();
        model.put("platformId","20200804322040900562534400");  //平台id

        //获取token,token可以多次使用，不用频繁获取
        String token = getToken();
//        String token="Bearer eyJ0eXAiOiJKc29uV2ViVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5rex5Zyz5biC5Lic5pa5572R6IGU5oqV6LWE5pyJ6ZmQ5YWs5Y-4LeS8gem5heW5s-WPsCIsInRva2VuX3R5cGUiOiJ0b2tlbiIsInVzZXJpZCI6IjMxMDUwMjgyMDYyODE5MzI4MCIsImFjY291bnQiOiJsaWNodWFueWluQGNyZWRsaW5rLmNvbSIsImV4cCI6MTU5NDMwOTgxNSwibmJmIjoxNTk0MjgxMDE1fQ.6bIb_E1vMynPPUtiYLk02Zd2gcoNXUS6ilfu6-kEKf4";

        //发送请求
        String result = HttpUtils.apiInvoke(URL, model,token);
        System.out.println(">>>>>>>>>>>>>>>>返回加密数据:"+result);

        //解密，验签返回的json
        String res = RsaUtils.decryptResJSONData(result);
        System.out.println("商户信息返回数据:"+res);
    }

    public static void main(String[] args) {
        apiInvoke();
    }

}
