package xin.h5.exception;

/**
 * 验证码发送失败
 */
public class SmsSendingFailedException extends Exception {
    
    // 构造器
    public SmsSendingFailedException(String message) {
        super(message);
    }

    // 如果你还想包含原始异常，可以添加一个带有cause的构造器
    public SmsSendingFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    // 可以根据需要添加更多的构造器或方法
}
