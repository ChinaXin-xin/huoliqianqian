package xin.admin.domain.fundsFlowManagement;

import lombok.Data;

import java.util.List;

/**
 * 用于pos之间的划拨
 */
@Data
public class Transfer {
    private String sn;
    private String phone;  //要转入人的手机号
    private String clazz;  //机具类型
    private String startSerial; //开始编号
    private String endSerial;   //结束编号
    private String[] transferList;  //划拨列表，要划拨的sn数组

    public String[] generateSerialNumbers() {

        if (startSerial.equals(endSerial)) {
            String[] t = new String[1];
            t[0] = startSerial;
            return t;
        }

        int prefixCount = -1; //前多少位空相同的，第一个下标为0
        for (int i = 0; i < endSerial.length(); i++) {
            if (startSerial.charAt(i) == endSerial.charAt(i)) {
                prefixCount++;
            } else {
                break;
            }
        }

        //相同的前缀
        String prefix = startSerial.substring(0, prefixCount);
        int startNum = Integer.parseInt(startSerial.substring(prefixCount));
        int endNum = Integer.parseInt(endSerial.substring(prefixCount));
        int count = endNum - startNum + 1;
        int suffixCount = startSerial.length() - prefixCount; //不相同的数量

        String[] strings = new String[count];
        for (int i = startNum; i <= endNum; i++) {
            String formattedNumber = String.format("%0" + suffixCount + "d", i);
            strings[i - startNum] = prefix + formattedNumber;
        }
        return strings;
    }

}
