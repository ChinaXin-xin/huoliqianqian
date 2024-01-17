package xin.yunhuo.api;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xin.yunhuo.base.TokenUtils;
import xin.yunhuo.domain.PaymentRecord;
import xin.yunhuo.domain.YunhuoResponseMsg;
import xin.yunhuo.utils.FileUtils;
import xin.yunhuo.utils.HttpUtils;
import xin.yunhuo.utils.RsaUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 单笔打款申请
 *
 * @author : chuanyin.li
 * @date: 2020/9/4
 */
public class YHV2Payment {

    /**
     * 接口地址
     */
    private final static String URL = "http://web.yunhuotec.com/api/thirdparty/payment/V2/pay";
    /**
     * 用户名
     */
    private final static String USERNAME = "FeiSuKeJi@yunhuo";
    /**
     * 密码
     */
    private final static String PASSWORD = "kQEj3wjCSEcYyP5j";

    private static final String IS_SUCCESS = "isSuccess";

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


    public static Boolean apiInvoke(PaymentRecord paymentRecord) {
        Map<String, Object> model = new HashMap<>(16);
        model.put("platformId", paymentRecord.getPlatformId());       // 平台id
        model.put("merOrderNo", paymentRecord.getMerOrderNo());       // 商户订单号
        model.put("inAcctName", paymentRecord.getInAcctName());       // 收款人姓名
        model.put("inCidno", paymentRecord.getInCidno());             // 收款人身份证号
        model.put("inMobile", paymentRecord.getInMobile());           // 收款人手机
        model.put("inAcctNo", paymentRecord.getInAcctNo());           // 收款人银行卡号
        model.put("amount", paymentRecord.getAmount());               // 打款金额
        model.put("remark", paymentRecord.getRemark());               // 打款备注
        model.put("cidAddress", paymentRecord.getCidAddress());       // 收款人地址

        // 这里假设发票编码和任务ID如果为空，则不添加到model中
        String invoiceCategory = paymentRecord.getInvoiceCategory();
        if (invoiceCategory != null && !invoiceCategory.isEmpty()) {
            model.put("invoiceCategory", invoiceCategory);             // 发票编码
        }

        String laborTaskId = paymentRecord.getLaborTaskId();
        if (laborTaskId != null && !laborTaskId.isEmpty()) {
            model.put("laborTaskId", laborTaskId);                     // 发票编码对应任务ID
        }

        String s1 = null;
        String s2 = null;
        try {
            // 身份证正面
            s1 = FileUtils.encodeBase64File(paymentRecord.getId_card_front());

            // 反面证正面
            s2 = FileUtils.encodeBase64File(paymentRecord.getId_card_back());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取token,token可以多次使用，不用频繁获取 有效期8小时
        String token = getToken();

        //发送请求
        String result = HttpUtils.apiFileInvoke(URL, model, token, s1, s2);
        System.out.println(">>>>>>>>>>>>>>>>返回加密数据:" + result);
        JSONObject jsonObject = JSON.parseObject(result);
        System.out.println(jsonObject);
        String res = "";
        if (jsonObject.getBoolean(IS_SUCCESS)) {
            res = RsaUtils.decryptResJSONData(ObjectUtil.toString(jsonObject.get("data")));
            System.out.println("商户打款返回数据:" + res);
        }

        paymentRecord.setYunhuoResponseMsg(JSON.parseObject(res, YunhuoResponseMsg.class));

        if ("120000".equals(paymentRecord.getYunhuoResponseMsg().getRspCod())) {
            System.out.println("业务提交成功！");
            return true;
        } else {
            System.out.println("业务提交失败！");
            return false;
        }
    }

    public static void main(String[] args) {
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setMerOrderNo(String.valueOf(System.nanoTime())); // 商户订单号
        paymentRecord.setInAcctName("李义新");                          // 收款人姓名
        paymentRecord.setInCidno("412722200401041518");                // 收款人身份证号
        paymentRecord.setInMobile("13223950610");                      // 收款人手机
        paymentRecord.setInAcctNo("6221804910030177269");              // 收款人银行卡号
        paymentRecord.setAmount(1L);                                 // 打款金额
        paymentRecord.setRemark("银行卡打款");                          // 打款备注
        paymentRecord.setCidAddress("河南郑州");                        // 收款人地址
        apiInvoke(paymentRecord);
    }
}
