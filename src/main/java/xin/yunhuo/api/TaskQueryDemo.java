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
 * @Author lizexiao
 * @Desc 用工任务信息查询调用Demo
 * @Date 2022/8/30 10:32
 */
public class TaskQueryDemo {

    private final static String URL="http://IP+端口/api/thirdparty/taskInfo/queryInvoiceSubjects";  //接口地址
    private final static String USERNAME="873418765001@qq.com";  //用户名
    private final static String PASSWORD="zuihou"; //密码


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
        Map<String,Object> model=new HashMap<>(2);
        //平台id
        model.put("platformId","20211026484478173631258624");
        // 客户请求订单号
        model.put("reqOrderNo", ""+System.currentTimeMillis());

        //获取token,token可以多次使用，不用频繁获取
        String token = getToken();
//        String token="Bearer eyJ0eXAiOiJKc29uV2ViVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5rex5Zyz5biC5Lic5pa5572R6IGU5oqV6LWE5pyJ6ZmQ5YWs5Y-4LeS8gem5heW5s-WPsCIsInRva2VuX3R5cGUiOiJ0b2tlbiIsInVzZXJpZCI6IjMxMDUwMjgyMDYyODE5MzI4MCIsImFjY291bnQiOiJsaWNodWFueWluQGNyZWRsaW5rLmNvbSIsImV4cCI6MTU5NDMwOTgxNSwibmJmIjoxNTk0MjgxMDE1fQ.6bIb_E1vMynPPUtiYLk02Zd2gcoNXUS6ilfu6-kEKf4";

        //发送请求
        String result = HttpUtils.jsonApiInvoke(URL, model, token);
        System.out.println(">>>>>>>>>>>>>>>>返回加密数据:" + result);
        JSONObject jsonObject = JSON.parseObject(result);
        System.out.println(jsonObject);
        if (jsonObject.getBoolean("isSuccess")) {
            //--------> : 解密
            String res = RsaUtils.decryptResJSONData(ObjectUtil.toString(jsonObject.get("data")));
            System.out.println("商户信息返回数据:"+res);
        }
    }

    public static void main(String[] args) {
        apiInvoke();
    }

}
