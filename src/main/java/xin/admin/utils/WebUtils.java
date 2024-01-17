package xin.admin.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtils
{
    /**
     * 将字符串渲染到客户端
     * 
     * @param response 渲染对象
     * @param json 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String json) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}