package xin.zhongFu.utils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.TreeMap;

public class SortUtil {
	
    /**
     * @Title: sort
     * @Description: 将
     */
    public static String sort(Object object) throws Exception {

         /*TreeMap 默认排序规则 ： 按照key的字典顺序来排序（升序）*/
        TreeMap<String, Object> map = new TreeMap<String, Object>();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            map.put(field.getName(),field.get(object));
            if ( "sign".equals(field.getName()) || "dataList".equals(field.getName()) ) {
                map.remove(field.getName());
            }
        }
        //依次取出KEY值组成待签名字符串
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter =  map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            Object value = map.get(key);
            if(value != null) {
                sb.append(value.toString());
            }
        }

        String msg = sb.toString().trim();
        return msg;
    }
    
}
