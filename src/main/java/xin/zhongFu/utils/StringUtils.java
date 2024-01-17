package xin.zhongFu.utils;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * 字符串工具类
 * @author nicho
 * @date 2021-10-08 11:00
 */
public class StringUtils {

    private static Pattern whitespacePattern = Pattern.compile("\\s");

    public static boolean containsWhitespace(String str) {
        requireNonNull(str);
        return whitespacePattern.matcher(str).find();
    }

    public static String join(String separator, List<String> input) {
        if (input == null || input.size() <= 0) return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            sb.append(input.get(i));
            // if not the last item
            if (i != input.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String genUrl(String url, String uri) {
        if(!url.endsWith("/")) url += "/";
        return url += uri;
    }

    public static Boolean isEmpty(String str) {
        if(str == null) return true;
        return "".equals(str.trim());
    }
    
    /**
     * 字符串[去除空格]是否为空，空为true
     *
     * @param str
     * @return boolean
     */
    public static boolean isBlank(String str) {
        if (StringUtils.isEmpty(str)) {
            return true;
        }
        str = str.trim();
        return isEmpty(str);
    }
    
    /**
     * 字符串[去除空格]是否不为空,有值为true
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    
    /**
     * bytes字符串转换为Byte值
     *
     * @param String
     *            src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        if((src.length() & 1) != 0)
            src = "0" + src;
        int l = src.length() / 2;

        byte[] ret = new byte[l];
        for (int i = 0; i < l; ++i) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = Integer.decode(
                    "0x" + src.substring(i * 2, m) + src.substring(m, n))
                    .byteValue();
        }
        return ret;
    }
    
    /**
     * bytes转换成十六进制字符串
     *
     * @param byte[] b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b) {
        if(b == null)
            return "";
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            // sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

}
