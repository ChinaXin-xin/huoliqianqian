package xin.admin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    /**
     * 针对中付的时间类型：20231218160401转2023-12-16 11:57:07
     * 将日期时间字符串转换为 "yyyy-MM-dd HH:mm:ss" 格式。
     * 输入格式应为 "yyyyMMddHHmmss" 或 "yyMMddHHmmss"。
     * 
     * @param input 日期时间字符串。
     * @return 转换后的日期时间字符串，格式为 "yyyy-MM-dd HH:mm:ss"。
     * @throws ParseException 如果输入字符串无法解析。
     */
    public static String formatDateTime(String input) throws ParseException {
        SimpleDateFormat inputFormat;
        if (input.length() == 14) {
            // 如果输入长度为14位，则假设格式为 "yyyyMMddHHmmss"
            inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        } else if (input.length() == 12) {
            // 如果输入长度为12位，则假设格式为 "yyMMddHHmmss"
            inputFormat = new SimpleDateFormat("yyMMddHHmmss");
        } else {
            // 如果不符合上述任何一种格式，抛出异常
            throw new ParseException("Invalid date format", 0);
        }

        // 设定输出格式为 "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 解析输入日期时间字符串
        Date date = inputFormat.parse(input);

        // 返回格式化后的日期时间字符串
        return outputFormat.format(date);
    }

    /**
     * 把Date转为2023-12-16 11:57:07这种类型
     * @param date
     * @return
     */

    public static String formatDate(Date date) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }

    // 测试方法
    public static void main(String[] args) {
        try {
            String formattedDate1 = formatDateTime("20231218160401");
            System.out.println(formattedDate1); // 输出 "2023-12-18 16:04:01"

            String formattedDate2 = formatDateTime("230218160401");
            System.out.println(formattedDate2); // 输出 "2023-02-18 16:04:01"
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
