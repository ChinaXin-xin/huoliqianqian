package xin.h5.zfb.utils.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AliJsonUtils {
	
	
	/**
	 * 把json字符串转换为实体类
	 * @param jsonStr
	 * @param obj
	 * @return
	 */
    public static<T> Object jsonToBean1(String jsonStr, Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr,
                    obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
	
	

	/**
	 * String转JSON字符串
	 * @param key
	 * @param value
	 * @return
	 */
	public static String stringToJsonByFastjson(String key, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>(16);
		map.put(key, value);
		return beanToJson1(map, null);
	}
	
	
	/**
	 * 实体类转json
	 * @param object
	 * @param dataFormatString
	 * @return
	 */
	public static String beanToJson1(Object object, String dataFormatString) {
		if (object != null) {
			if (StringUtils.isEmpty(dataFormatString)) {
				return JSONObject.toJSONString(object);
			}
			return JSON.toJSONStringWithDateFormat(object, dataFormatString);
		} else {
			return null;
		}
	}
	
	
	
	/**
     * 字符串转成json对象
     * @param jsonString
     * @return
     */
    public static Object getJsonObject(String jsonString) {
    	return JSONArray.parse(jsonString);
    }
    
    
    /**
     * json字符串转集合对象
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }
    
    
    
    //=============================json字符串转换为map的方法========================
    /**
     * 这个是用JSON类来解析JSON字符串!!!
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonStringToMap1(String jsonString){
    	return (Map)JSON.parse(jsonString);
    }
    
    
    /**
     * 这个是用JSON类的parseObject来解析JSON字符串!!!
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonStringToMap2(String jsonString){
    	return JSON.parseObject(jsonString);
    }
    
    
    /**
     * 这个是用JSON类,指定解析类型，来解析JSON字符串!!!
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonStringToMap3(String jsonString){
    	return JSON.parseObject(jsonString,Map.class);
    }
    
    
    /**
     * 这个是用JSONObject类的parse方法来解析JSON字符串!!!
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonStringToMap4(String jsonString){
    	return (Map) JSONObject.parse(jsonString);
    }
    
    
    /**
     * 这个是用JSONObject的parseObject方法来解析JSON字符串!!!
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonStringToMap5(String jsonString){
    	return JSONObject.parseObject(jsonString);
    }
    
    
    /**
     * 这个是用JSONObject的parseObject方法并执行返回类型来解析JSON字符串!!!
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonStringToMap6(String jsonString){
    	return JSONObject.parseObject(jsonString,Map.class);
    }
    
    
    public static void main(String[] args) {
		System.out.println(jsonStringToMap1(""));
	}
}
