package xin.h5.zfb.entity;

import java.util.HashMap;

public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public static final String SUCCESS_TAG = "success";

    public static final String CODE_TAG = "code";

    public static final String MSG_TAG = "msg";

    public static final String DATA_TAG = "data";
    
    
    /**
     * 状态类型
     */
    public enum Type
    {
        /** 成功 */
        SUCCESS(true),
        /** 警告 */
        FAILE(false);
    	
        public final boolean value;

        Type(boolean value)
        {
            this.value = value;
        }

        public boolean value()
        {
            return this.value;
        }
    }


	/**
     * 错误提示：消息码
     * @param code
     * @param msg
     * @return
     */
	public static R error(String code) {
		R r = new R();
		r.put(SUCCESS_TAG, Type.FAILE.value);
		r.put(CODE_TAG, code);
		return r;
	}
	

    /**
     * 错误提示：消息码+消息
     * @param code
     * @param msg
     * @return
     */
	public static R error(String code, String msg) {
		R r = new R();
		r.put(SUCCESS_TAG, Type.FAILE.value);
		r.put(CODE_TAG, code);
		r.put(MSG_TAG, msg);
		return r;
	}
	
	
	/**
     * 错误提示：消息码+消息+数据
     * @param code
     * @param msg
     * @return
     */
	public static R error(String code, String msg, Object data) {
		R r = new R();
		r.put(SUCCESS_TAG, Type.FAILE.value);
		r.put(CODE_TAG, code);
		r.put(MSG_TAG, msg);
		r.put(DATA_TAG, data);
		return r;
	}
	
	
	/**
	 * 正确提示：消息码
	 * @param code
	 * @return
	 */
	public static R ok(String code) {
		R r = new R();
		r.put(SUCCESS_TAG, Type.SUCCESS.value);
		r.put(SUCCESS_TAG, true);
		r.put(CODE_TAG, code);
		return r;
	}
	
	
	/**
	 * 正确提示：消息码+消息
	 * @param code
	 * @param msg
	 * @return
	 */
	public static R ok(String code, String msg) {
		R r = new R();
		r.put(SUCCESS_TAG, Type.SUCCESS.value);
		r.put(CODE_TAG, code);
		r.put(MSG_TAG, msg);
		return r;
	}
	
	
	/**
	 * 正确提示：消息码+消息+数据
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 */
	public static R ok(String code, String msg, Object data) {
		R r = new R();
		r.put(SUCCESS_TAG, Type.SUCCESS.value);
		r.put(CODE_TAG, code);
		r.put(MSG_TAG, msg);
		r.put(DATA_TAG, data);
		return r;
	}
	

	/**
	 * put值
	 */
	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	
	
	public static void main(String[] args) {
		R r = R.ok("999997","错误啦");
		System.out.println(r);
		System.out.println(Boolean.valueOf(r.get("success").toString())==false);
	}
	
}
