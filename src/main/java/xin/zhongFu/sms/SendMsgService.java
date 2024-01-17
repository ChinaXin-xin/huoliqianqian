package xin.zhongFu.sms;

import com.alibaba.fastjson.JSONObject;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SendMsgService {

    //jxl只支持97-2003版本的excel，需要转换为xls格式
    public static void main(String[] args){
        String message = "畅捷支付为积极响应四部委发布的《关于降低小微企业和个体工商户支付手续费的通知》（银发〔2021〕169号）的工作要求，于2021年9月30日至2024年9月29日期间，对符合标准的商户实行支付手续费优惠政策，具体降费公告关注微信公众号“畅捷支付商户通”查看。";

        List<String> numbers = readExcel("/Users/lipengpeng/Desktop/1.xls");
        System.out.println("数据列表大小:"+numbers.size());
        for (int i = 0; i < numbers.size(); i++) {
            JSONObject body = new JSONObject();
            body.put("clientNo", "1007");
            body.put("traceNo", "20230721150123" + i);
            body.put("mobile", numbers.get(i));
            body.put("message", message);
            body.put("sendType", "1");
            body.put("channelId", "ZHANGXUN");
            body.put("messageSign", "畅捷支付");
            System.out.println("请求信息:"+body.toJSONString());
            ResponseEntity response = RestTemplateUtils.post("http://10.255.2.67:8769/smsMessage/send", body.toJSONString());
            System.out.println("返回信息:"+response);
            if (!ResponseCodeEnum.SERVICE_OK.getCode().equals(response.getCode())){
                System.out.println("请求失败，CODE" + response.getCode() + ", MESSAGE: " + response.getMessage());
            }
        }
        
    }

    public static List<String> readExcel(String fileDir) {
        Workbook wb = null;
        List<String> numbers = new ArrayList<>();
        try {
            File file = new File(fileDir);
            wb = Workbook.getWorkbook(file);
            Sheet sheet  = wb.getSheet(0);
            String number = "";
            for (int row = 1; row < sheet.getRows(); row++) {
                for (int col = 0; col < sheet.getColumns(); col++) {
                    String cellinfo = sheet.getCell(col, row).getContents();
                    numbers.add(cellinfo);
                    /**
                    number += cellinfo + ",";
                    if(row%100==0 || row==sheet.getRows()-1){
                        String line = number.substring(0, number.length()-1);
                        numbers.add(line);
                        number = "";
                    }
                    */
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numbers;
    }

}
