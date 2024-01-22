package xin.common.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xin.admin.filter.JwtAuthenticationTokenFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // 继承WebSecurityConfigurerAdapter来自定义安全配置

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;  // 自动注入JWT认证过滤器

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    AccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 定义密码编码器，使用BCrypt强哈希算法
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // 重写方法以返回AuthenticationManager，用于处理认证请求
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 定义如何通过拦截器保护请求
        http
                .csrf().disable()  // 禁用CSRF保护
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 设置为无状态，不依赖Session
                .and()
                .authorizeRequests()  // 开始定义URL保护规则
                .antMatchers("/api/image/**").permitAll()  // 允许对 /api/image/ 下的所有请求放行
                .antMatchers("/admin/sysBanner/list").permitAll()  // 允许对 /api/image/ 下的所有请求放行
                .antMatchers("/admin/CommercialTenantOrderZF/add").permitAll()  // 允许对 /api/image/ 下的所有请求放行
                .antMatchers(HttpMethod.POST, "/admin/aliReturnPay/returnPayAsynchronous").permitAll()  // 支付宝异步回调
                .antMatchers("/admin/aliReturnPay/returnPaySynchronization").permitAll()  // 支付宝同步跳转
                .antMatchers("/alipay/aliPayQuitUrl").permitAll()  // 支付宝交易取消
                .antMatchers(HttpMethod.POST, "/admin/ping/*").permitAll() // 允许对 /api/image/ 下的所有请求放行
                .antMatchers(HttpMethod.POST, "/h5/login").permitAll() // 允许对 /api/image/ 下的所有请求放行
                .antMatchers(HttpMethod.POST, "/h5/sms/**").permitAll() // 允许对 /api/image/ 下的所有请求放行
                .antMatchers(HttpMethod.POST, "/h5/common/**").permitAll() // 公告，轮播图等公共的资源
                .antMatchers(HttpMethod.POST, "/pro/getSessionKeyAndOpenId").permitAll() // 用户用去session_key
                .antMatchers(HttpMethod.POST, "/pro/login").permitAll() // 公告，轮播图等公共的资源
                .antMatchers(HttpMethod.POST, "/h5/account/**").permitAll() // 允许对 /api/image/ 下的所有请求放行
                .antMatchers(HttpMethod.POST, "/admin/bindMachineInformZF/add").permitAll() // 中付推送的绑机通知
                .antMatchers(HttpMethod.POST, "/api/uploadBase64").permitAll() // 中付推送的绑机通知
                .antMatchers(HttpMethod.POST, "/pro/homePage/getRandomRecommendedItems").permitAll() // 微信小程序首页好物推荐10条数据
                .antMatchers(HttpMethod.POST, "/pro/pay/notify").permitAll() // 微信小程序首页好物推荐10条数据
                .antMatchers("/pro/pay/notify").permitAll() // 微信小程序首页好物推荐10条数据
                .antMatchers("/static/**").permitAll() // 允许对 /static/ 下的所有静态资源放行
                .antMatchers("/admin/login").permitAll()  // 只有不认证的才可以访问
                .antMatchers(HttpMethod.POST, "/pro/commodityClassification/select").permitAll()  // 查询所有分类
                .antMatchers(HttpMethod.POST, "/pro/commodityClassification/selectByAllMsg/*").permitAll()  // 根据分类id查询
                .antMatchers(HttpMethod.POST, "/admin/commodityDetail/selectByDetails").permitAll()  // 查询商品关键字
                .antMatchers( "/admin/homePage/notification").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/homePage/notification").permitAll()
                .antMatchers(HttpMethod.POST, "/admin/hi").permitAll()
                .antMatchers( "/admin/hi").permitAll()

                .antMatchers("/admin/register").anonymous()
                .anyRequest().authenticated()  // 其他所有请求都需要认证
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //配置登录异常与权限不足的处理器
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        http.cors();
    }
}
