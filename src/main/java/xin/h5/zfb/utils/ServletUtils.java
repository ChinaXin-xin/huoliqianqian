package xin.h5.zfb.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import xin.h5.zfb.utils.string.StringUtil;
import xin.h5.zfb.utils.text.Convert;


/**
 * 客户端工具类
 * 
 * @author ruoyi
 */
public class ServletUtils
{
    /**
     * 获取String参数
     */
    public static String getParameter(String name)
    {
        return getRequest().getParameter(name);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue)
    {
        return Convert.toStr(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name)
    {
        return Convert.toInt(getRequest().getParameter(name));
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue)
    {
        return Convert.toInt(getRequest().getParameter(name), defaultValue);
    }

    
    /**
     * 获取request
     */
    public static HttpServletRequest getRequest()
    {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse()
    {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession()
    {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes()
    {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将字符串渲染到客户端
     * 
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string)
    {
        try
        {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    
    /**
     * 是否是Ajax异步请求
     * 
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request)
    {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1)
        {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
        {
            return true;
        }

        String uri = request.getRequestURI();
        if (StringUtil.inStringIgnoreCase(uri, ".json", ".xml"))
        {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if (StringUtil.inStringIgnoreCase(ajax, "json", "xml"))
        {
            return true;
        }
        return false;
    }
    
    
    /**
     * request.getHeader() 相关详细方法描述
     * @param request
     */
    public static void getHeader(HttpServletRequest request,HttpSession session)
    {
    	 //获取客户端向服务器端传送数据的协议名称
    	String Rotocol = request.getProtocol();
    	//返回的协议名称，默认是http
    	String Scheme = request.getScheme();
    	//可以返回当前页面所在的服务器的名字;如果你的应用部署在本机那么其就返回localhost或者127.0.0.1 ，这两个是等价的
    	String ServerName = request.getServerName();
    	//可以返回当前页面所在的服务器使用的端口,就是8083
    	int ServerPort = request.getServerPort();
    	//获得客户端的ip地址
    	String RemoteAddress = request.getRemoteAddr();
    	//获得客户端的主机名
    	String RemoteHost = request.getRemoteHost();
    	//返回字符编码
    	String CharacterEncoding = request.getCharacterEncoding();
    	//
    	int ContentLength = request.getContentLength();
    	//定义网络文件的类型和网页的编码，决定浏览器将以什么形式、什么编码读取这个文件
    	String ContentType = request.getContentType();
    	//如果servlet由一个鉴定方案所保护，如HTTP基本鉴定，则返回方案名称
    	String AuthType = request.getAuthType();
    	//返回HTTP请求方法（例如GET、POST等等）
    	String HttpMethod = request.getMethod();
    	//返回在URL中指定的任意附加路径信息。
    	String PathInfo = request.getPathInfo();
    	//返回在URL中指定的任意附加路径信息，被子转换成一个实际路径
    	String PathTrans = request.getPathTranslated();
    	//返回查询字符串，即URL中?后面的部份。
    	String QueryString = request.getQueryString();
    	//如果用户通过鉴定，返回远程用户名，否则为null。
    	String RemoteUser = request.getRemoteUser();
    	//返回客户端的会话ID
    	String SessionId = request.getRequestedSessionId();
    	//返回URL中一部分，从“/”开始，包括上下文，但不包括任意查询字符串。
    	String RequestURI = request.getRequestURI();
    	//返回请求URI上下文后的子串
    	String ServletPath = request.getServletPath();
    	
    	//返回指定的HTTP头标指。如果其由请求给出，则名字应为大小写不敏感。
    	String Accept = request.getHeader("Accept");
    	//获取请求的头部信息
    	String Host = request.getHeader("Host");
    	//获取来源页地址
    	String Referer = request.getHeader("Referer");
    	//获取请求方地址
    	String Origin = request.getHeader("Origin");
    	//获取请求方语言
    	String AcceptLanguage = request.getHeader("Accept-Language");
    	//
    	String AcceptEncoding = request.getHeader("Accept-Encoding");
    	//
    	String UserAgent = request.getHeader("User-Agent");
    	//
    	String Connection = request.getHeader("Connection");
    	//获取请求方Cookie
    	String Cookie  = request.getHeader("Cookie");
    	//获取请求方时间
    	long CreatedTime = session.getCreationTime();
    	//
    	long LastAccessed = session.getLastAccessedTime();
    }
}
