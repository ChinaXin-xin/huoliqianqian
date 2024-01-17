package xin.zhongFu.model.resp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

public class BaseRespEntity implements Serializable {

    private static final long serialVersionUID = -3822126284142862179L;
    private String code;
    private String message;
    private String data;
    private Long total;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
    @Override
	public String toString() {
		return JSON.toJSONString(this,
			new SerializerFeature[] {
					SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
					SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero,
					SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.UseISO8601DateFormat
			});
	}
}
