package xin.zhongFu.exception;

import lombok.Data;

@Data
public class AcqBizException extends Exception {

    /**
     * 异常摘要，后台日志打印，不写入响应
     */
    private String digest;

    /**
     * 有参的构造方法
     */
    public AcqBizException(String msg) {
        super(msg);
        this.digest = msg;
    }

    public AcqBizException(String message, String digest) {
        super(message);
        this.digest = message + digest;
    }

    /**
     * 用指定的详细信息和原因构造一个新的异常
     */
    public AcqBizException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 用指定原因构造一个新的异常
     */
    public AcqBizException(Throwable cause) {
        super(cause);
    }
}
