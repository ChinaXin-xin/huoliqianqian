package xin.h5.zfb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 继承WebMvcConfigurerAdapter，通过重写父类的方法进行扩展从而新增一个拦截器
 * @author Administrator
 *
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter{
	
	
	/**
	 * 设置默认访问页面
	 */
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/toIndex");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
	
	
	/**
     * 配置静态访问资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
    	registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    	/** 文件上传路径 */
        registry.addResourceHandler("/profile/**").addResourceLocations("file:" + RuoYiConfig.getProfile());

        /** swagger配置 */
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
	
	
	
	/**
	 * 实现添加拦截器方法
	 */
	/*@Override  
	public void addInterceptors(InterceptorRegistry registry)
	{  
		//可以通过此方法添加拦截器, 可以是spring提供的或者自己添加的  
		registry.addInterceptor(new FrontLoginInterceptor())
				.addPathPatterns("/front/**");
				//.excludePathPatterns("/front/login/*"); 
	}*/
}
