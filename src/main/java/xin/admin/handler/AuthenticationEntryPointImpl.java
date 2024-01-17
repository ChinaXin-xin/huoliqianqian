package xin.admin.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import xin.admin.domain.ResponseResult;
import xin.admin.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseResult responseResult;

        if (authException instanceof BadCredentialsException) {
            responseResult = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "账号密码错误");
        } else if (authException instanceof CredentialsExpiredException) {
            responseResult = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "Token过期");
        } else if (authException instanceof DisabledException) {
            responseResult = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "账户被禁用");
        } else if (authException instanceof AccountExpiredException) {
            responseResult = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "账户过期");
        } else if (authException instanceof LockedException) {
            responseResult = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "账户被锁定");
        } else {
            // 其他认证失败情况
            responseResult = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "认证失败：" + authException.getMessage());
        }

        // 将错误信息转换为JSON并发送给客户端
        WebUtils.renderString(response, JSON.toJSONString(responseResult));
    }
}
