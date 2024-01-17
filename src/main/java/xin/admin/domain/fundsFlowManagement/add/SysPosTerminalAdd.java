package xin.admin.domain.fundsFlowManagement.add;

import lombok.Data;

import java.util.Arrays;

/**
 * 用于接收前端添加SN库存信息的表单
 */

@Data
public class SysPosTerminalAdd {

    public static final Integer uid = 100;  //默认绑定的的是管理员

    private Byte ver;  //机具版本
    private String classType;  //机具类型值   1天喻  2锦弘霖
    private String startSerial; //开始编号
    private String endSerial;   //结束编号
    private String clazz;

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

/*        System.out.println("长度：" + (startSerial.length()));
        System.out.println("相同前缀长度：" + prefixCount);
        System.out.println("后缀长度：" + suffixCount);
        System.out.println("前缀：" + prefix);
        System.out.println("一共多少个：");*/

        String[] strings = new String[count];
        for (int i = startNum; i <= endNum; i++) {
            String formattedNumber = String.format("%0" + suffixCount + "d", i);
            strings[i - startNum] = prefix + formattedNumber;
        }
        return strings;
    }


    public static void main(String[] args) {
        SysPosTerminalAdd sysPosTerminalAdd = new SysPosTerminalAdd();
        sysPosTerminalAdd.setStartSerial("003");
        sysPosTerminalAdd.setEndSerial("005");
        String[] serialNumbers = sysPosTerminalAdd.generateSerialNumbers();
        Arrays.stream(serialNumbers).forEach(System.out::println);
    }
}
