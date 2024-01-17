package xin.yunhuo.utils;


import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

/**
 * @Author Huangdaye
 * @Desc
 * @Date 2020/7/13 10:31
 */
public class FileUtils {

    /**
     * 文件转Base64 Base64字符和文件名用 ","分隔
     * @author zhangpengfei
     * @date 2022/3/1 11:06
     * @param path:
     * @return: java.lang.String
     */
    public static String fileToBase64(String path) {
        File file = FileUtil.file(path);
        String name = file.getName();
        String encode = cn.hutool.core.codec.Base64.encode(file);
        return encode + "," + name;
    }

    /**
     * @Author huangdaye
     * @Description 图片文件转base64编码
     * @Date 10:32 2020/7/13
     * @Param [path]
     * @return java.lang.String
     **/
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return "data:image/jpg;base64,"+Base64.getEncoder().encodeToString(buffer);
    }

    public static void main(String[] args) {
        try {
            System.out.println(encodeBase64File("E:\\workspace\\IDEA\\system\\cloudgain-cloud-platform\\cloudgain-client-api\\src\\main\\resources\\img\\111.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
