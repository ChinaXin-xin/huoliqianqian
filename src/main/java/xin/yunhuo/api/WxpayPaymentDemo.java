package xin.yunhuo.api;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xin.yunhuo.base.TokenUtils;
import xin.yunhuo.utils.FileUtils;
import xin.yunhuo.utils.HttpUtils;
import xin.yunhuo.utils.RsaUtils;

import java.util.HashMap;
import java.util.Map;

/**
*  微信单笔打款申请
* @author : chuanyin.li
* @date: 2020/9/4
*/
public class WxpayPaymentDemo {

    /**
     * 接口地址
     */
    private final static String URL = "http://10.18.6.13:10000/api/thirdparty/payment/wechatpayApply";
    /**
     * 用户名
     */
    private final static String USERNAME = "zhangpengfei@credlink.com";
    /**
     * 密码
     */
    private final static String PASSWORD = "zuihou";
    private static final String IS_SUCCESS = "isSuccess";

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
        Map<String,Object> model=new HashMap<>(16);
        model.put("platformId","20200720316539517784424448");  //平台id
        //商户订单号
        model.put("merOrderNo",System.nanoTime()+"");
        //收款人姓名
        model.put("inAcctName","关键彤");
        //收款人身份证号
        model.put("inCidno","440681199605175940");
        //收款人手机
        model.put("inMobile","15626269234");
        //收款人微信openid
        model.put("inAcctNo","oafm2s-Amo8e536kRLxRMLwXci3Q");
        //打款金额
        model.put("amount",10L);
        //打款备注
        model.put("remark","微信打款single");
        //打款备注
        model.put("cidAddress","广东深圳");

        //发票编码和任务可调用TaskQueryDemo进行选取
        //发票编码
        model.put("invoiceCategory","01");
        //发票编码对应任务ID
        model.put("laborTaskId","20220920603642291112448000");

        String s1 = null;
        String s2 = null;
        try {
            //人像面
            s1 = FileUtils.encodeBase64File("F:\\xgdfin\\OCR类产品\\身份证OCR\\信联自研\\WeChat Image_20200214200106.jpg");
            //国徽面
            s2 = FileUtils.encodeBase64File("F:\\xgdfin\\OCR类产品\\身份证OCR\\信联自研\\WeChat Image_20200214200106.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取token,token可以多次使用，不用频繁获取 有效期8小时
        String token = getToken();
//        String token="Bearer eyJ0eXAiOiJKc29uV2ViVG9rZW4iLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5rex5Zyz5biC5Lic5pa5572R6IGU5oqV6LWE5pyJ6ZmQ5YWs5Y-4LeS8gem5heW5s-WPsCIsInRva2VuX3R5cGUiOiJ0b2tlbiIsInVzZXJpZCI6IjMxMDUwMjgyMDYyODE5MzI4MCIsImFjY291bnQiOiJsaWNodWFueWluQGNyZWRsaW5rLmNvbSIsImV4cCI6MTU5NDYzNDgwMywibmJmIjoxNTk0NjA2MDAzfQ.h87SLSsjUIGXua2S1OYdoFd2-zbS2YDDNa3Zm7irE6I";

        //发送请求
        String result = HttpUtils.jsonApiInvoke(URL, model,token,s1,s2);
        System.out.println(">>>>>>>>>>>>>>>>返回加密数据:"+result);
        JSONObject jsonObject = JSON.parseObject(result);
        System.out.println(jsonObject);
        if(jsonObject.getBoolean(IS_SUCCESS)){
            //--------> : 解密
            String res = RsaUtils.decryptResJSONData(ObjectUtil.toString(jsonObject.get("data")));
            System.out.println("商户打款返回数据:"+res);
        }
    }

    public static void main(String[] args) {
        apiInvoke();
//        String reCallRes ="{\"encrypt\":\"FyL20QMJWHgDje8RZd0EJ6+4PZNCSkCaJNLQrmGW/djgEPjV7SWIbG89R5YZ4nXBcsu9DAeMy6cs28Uuw85f+X06Ff5EPvXDBXUFh9lEPvYkxU85i/uSGqGniMtERZ6MQewupNKudoQxV+iKicrZO4FS/cvbBIa4hMgfwsEUHPgGD+c3/osr7dSnxJET0hwOYN9o2Gjjoi/BWTnNxKb0FDsaBpdn6xzWIe6jMmU6V1q1v/x+9i9ZokGO+PXAOQHwaIc40B+5IxZUMTQAZy0ESJwYQ2mArIcsKuZ21NuSoPurlUp+NB96Rv+SQYcXLPWWv7RK0V+14JTWPhK9u33MQg==\",\"sign\":\"GbZPZIZR3+whX9s+3p1Jx9LeJXWwu8VE0jImCPs21h4d5HwDxnzZXG8uPnhHj4SuT3rtPVLCXY7euyHFK3IEGwetunoWY7kZsQtRm3nXJX3HYzg2XXvJyB3fGNGAV7gvZ81fZXu1uCAYU3AqGcd0RxvsevxwN5/PQI7DzD8vqDA=\"}";
//        String s = RsaUtils.decryptResJSONData(reCallRes);
//        System.out.println("回调信息解密-->"+s);

    }
}
