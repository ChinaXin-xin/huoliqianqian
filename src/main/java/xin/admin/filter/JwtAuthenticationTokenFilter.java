package xin.admin.filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import xin.admin.domain.LoginUser;
import xin.admin.mapper.UserMapper;
import xin.admin.utils.JwtUtil;
import xin.common.domain.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        //String token = request.getHeader("token");
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }


        // 假设原始令牌是 ISO-8859-1 编码，并将其转换为 UTF-8!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        byte[] bytes = token.getBytes(StandardCharsets.ISO_8859_1);
        token = new String(bytes, StandardCharsets.UTF_8);

        //[0]:用户名，[1]:登录密码，[2]:jwt
        String[] parts;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String jwtNode = claims.getSubject();

            //[0]:用户名，[1]:登录密码，[2]:jwt
            parts = jwtNode.split("\\+\\+--\\+\\+");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

        //每次都从数据库中获取，避免redis与mysql数据不同步
        User user = userMapper.queryByUserPassword(parts[0], parts[1]);

        if (Objects.isNull(user)) {
            //throw new RuntimeException("token非法，或者用户登录超时");
            throw new BadCredentialsException("账号密码错误");
        }

        if (!user.getJwt().equals(parts[2])) {
            throw new CredentialsExpiredException("Token过期");
        }

        LoginUser loginUser = new LoginUser(user);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //请求放行
        filterChain.doFilter(request, response);
    }
}
