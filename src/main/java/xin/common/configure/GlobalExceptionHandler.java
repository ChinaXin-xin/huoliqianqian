package xin.common.configure;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.admin.domain.ResponseResult;
import xin.common.exception.MyCustomException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public ResponseResult handleCustomException(MyCustomException e) {
        return new ResponseResult(e.getCode(), e.getMsg());
    }
}
