package xin.h5.zfb.utils.string;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import xin.h5.zfb.utils.text.StrFormatter;

/**
 * 字符串工具类
 * 
 * @author ruoyi
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils
{
	
	private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	private static BigDecimal bigdecimal_0 = new BigDecimal(0);
	
	/**
	 * 钱包地址加密
	 */
	private static String chs="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	
    /** 空字符串 */
    private static final String NULLSTR = "";

    /** 下划线 */
    private static final char SEPARATOR = '_';
    
    
    /**
	 * 对map里面参数处理，有返回该值，无返回""
	 * @param map
	 * @param paramName
	 * @return
	 */
	public static String getMapValue(Map<String, Object> map,String paramName) {
		if(map!=null&&map.containsKey(paramName)&&map.get(paramName)!=null) {
			return map.get(paramName).toString();
		}else {
			return "";
		}
	}
	
	
	/**
	 * 对map里面参数处理，有返回该值，无返回""
	 * @param map
	 * @param paramName
	 * @return
	 */
	public static String getMapStringValue(Map<String, String> map,String paramName) {
		if(map!=null&&map.containsKey(paramName)&&map.get(paramName)!=null) {
			return map.get(paramName);
		}else {
			return "";
		}
	}
	
	
	/**
	 * 判断某个字符串中是否包含另外一个字符串
	 * @param fatherString
	 * @param sonString
	 * @return
	 */
	public static boolean containString(String fatherString,String sonString){
		if(fatherString.indexOf(sonString)!=-1){  
			System.out.println("包含"); 
			return true;
		}else{ 
			System.out.println("不包含"); 
			return false;
		} 
	}
	
	
	/**
	 * 生成随机ID（23）
	 * @return
	 */
	public static String getDateTimeAndRandomForID(){
		int random = (int)((Math.random()*9+1)*100000);
		String id = format.format(new Date())+random;
		return id;
	}
	
	
	/**
	 * 生成32位钱包地址
	 * @param len
	 * @return
	 */
	public static String getUserAddress(int len){
		char[] code=new char[len];
		Random random = new Random();
		for(int i=0; i<code.length; i++){
			code[i]=chs.charAt(random.nextInt(chs.length()));
		}
		return new String(code); 
	}
	
	
	/**
	 * 生成Token参数
	 * @return
	 */
	public static String getTokenRandom(String userId){
		String randomcode = "";
		// 用字符数组的方式随机
		String model = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] m = model.toCharArray();
		for (int j = 0; j < 32; j++) {
			char c = m[(int) (Math.random() * 36)];
			randomcode = randomcode + c;
		}
		return userId+"|"+randomcode;
	}
	
    
    
    /**
	 * 生成纯数字
     * @param num 
	 * @return
	 */
	public static String getCode(int num) {
		Long time = System.currentTimeMillis();
		String strTime = String.valueOf(time);
		String code = strTime.substring(strTime.length() - num);
		return code;
	}
	
	
	/**
	 * 判断数值是否大于零
	 * @param num
	 * @return
	 */
	public static boolean isValidLargeBigDecimal0(String num){
		try{
			if(StringUtils.isEmpty(num)){
				return false;
			}else{
				if(new BigDecimal(num).compareTo(bigdecimal_0)>0){
					return true;
				}else{
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * 判断ETH地址是否规范
	 * @param address
	 * @return
	 */
	public static boolean isValidETHAddress(String address){
		try{
			if("0x".equals(address.substring(0,2))&&address.length()==42){
				return true;
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     * 
     * @param string
     * @param str1
     * @param str2
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {
 
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);
 
        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }
    
    
    /**
     * 将字符串首字母大写
     * @param name 需要修改的字符串参数
     * @return 将name返回
     */
    public static String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return  name;

    }
    /**
     * 首字母大写
     *
     * @param srcStr
     * @return
     */
    public static String firstCharacterToUpper(String srcStr) {
        return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
    }
    
    
    /**
     * 替换字符串并让它的下一个字母为大写
     * @param srcStr
     * @param org
     * @param ob
     * @return
     */
    public static String replaceUnderlineAndfirstToUpper(String srcStr,String org,String ob)
    {
        String newString = "";
        int first=0;
        while(srcStr.indexOf(org)!=-1)
        {
            first=srcStr.indexOf(org);
            if(first!=srcStr.length())
            {
                newString=newString+srcStr.substring(0,first)+ob;
                srcStr=srcStr.substring(first+org.length(),srcStr.length());
                srcStr=firstCharacterToUpper(srcStr);
            }
        }
        newString=newString+srcStr;
        return newString;
    }
    
    

    /**
     * 获取参数不为空值
     * 
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue)
    {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     * 
     * @param objects 要判断的对象数组
     ** @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     * 
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     * 
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     * 
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     * 
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     * 
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     * 
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object)
    {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @param end 结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     * 
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params 参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... params)
    {
        if (isEmpty(params) || isEmpty(template))
        {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * 下划线转驼峰命名
     */
    public static String toUnderScoreCase(String str)
    {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    
    /**
     * 是否包含字符串
     * 
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /**
     * 字符串小写全部转为大写
     * @param str
     * @return
     */
    public static String ToUpper(String str) {
    	return str.toUpperCase();
    }
    
    
    /**
     * 获得.以及后面的字符（通常用作文件后缀）
     * @param str
     * @return
     */
    public static String getLastStrAfter(String str) {
    	int one = str.lastIndexOf(".");
    	return "."+str.substring((one+1),str.length());
    }
    
    
    /**
     * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
     * @author zxcvbnmzb
     * @param testStr 要+1的字符串
     * @return +1后的字符串
     * @exception NumberFormatException
     */
     public static String addOne(String testStr){
         String[] strs = testStr.split("[^0-9]");//根据不是数字的字符拆分字符串
         String numStr = strs[strs.length-1];//取出最后一组数字
         if(numStr != null && numStr.length()>0){//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
             int n = numStr.length();//取出字符串的长度
             Long num = Long.parseLong(numStr)+1;//将该数字加一
             String added = String.valueOf(num);
             n = Math.min(n, added.length());
             //拼接字符串
             return testStr.subSequence(0, testStr.length()-n)+added;
         }else{
             throw new NumberFormatException();
         }
     }
     
     
     /**
      * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
      * @author zxcvbnmzb
      * @param testStr 要+1的字符串
      * @return +1后的字符串
      * @exception NumberFormatException
      */
      public static String addOneForTen(String testStr){
          String[] strs = testStr.split("[^0-9]");//根据不是数字的字符拆分字符串
          String numStr = strs[strs.length-1];//取出最后一组数字
          int n = 0;
          if(numStr != null && numStr.length()>0){//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
              if(numStr.length()>10){//超过十位按十位自增
             	 n = 10;
             	 numStr = testStr.substring(testStr.length()-n,testStr.length());
              }else{
             	 n = numStr.length();//取出字符串的长度
              }
              Long num = Long.parseLong(numStr)+1;//将该数字加一
              String added = String.valueOf(num);
              n = Math.min(n, added.length());
              //拼接字符串
              return testStr.subSequence(0, testStr.length()-n)+added;
          }else{
              throw new NumberFormatException();
          }
     }
    
    

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     * 
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name)
    {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty())
        {
            // 没必要转换
            return "";
        }
        else if (!name.contains("_"))
        {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels)
        {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty())
            {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }
    
    /**
     * 校验数组中是否有存在context
     * @param str
     * @param context
     * @return
     */
    public static boolean checkStringArray(String[] strList, String context) {
    	for(String str : strList) {
    		if(str.equals(context)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public static void main(String[] args) {
		System.out.println(getUserAddress(72));
	}
}