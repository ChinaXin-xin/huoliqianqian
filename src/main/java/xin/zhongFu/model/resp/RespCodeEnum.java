package xin.zhongFu.model.resp;

public enum RespCodeEnum {

    SERVICE_OK("00", "操作成功"),
    SERVICE_BUSINESS_ERROR("20", "业务异常"),
    SERVICE_OTHER_ERROR("30", "系统异常"),
    SERVICE_VALIDATED_ERROR("40", "校验异常"),
    SERVICE_NETSTAT_ERROR("50", "网络异常,请稍后重试");

    String code;
    String msg;

    RespCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return this.code + ":" + this.msg;
    }
}
