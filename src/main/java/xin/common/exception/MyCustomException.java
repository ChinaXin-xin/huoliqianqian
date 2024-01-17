package xin.common.exception;

public class MyCustomException extends RuntimeException {
    private final int code;
    private final String msg;

    public MyCustomException(int code, String msg) {
        super(msg); // 调用父类构造器设置异常消息
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
