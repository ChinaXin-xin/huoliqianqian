package xin.h5.zfb.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 系统监控：URL耗时监听
 * @author Administrator
 *
 */
@Aspect
@Component
public class WebLogAspect {
	
	@Pointcut("execution(* com.example.learn.controller..*..*(..))")//两个..代表所有子目录，最后括号里的两个..代表所有参数
	public  void logPointCut() {
	
	}
	   
	@Before("logPointCut()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		// 记录下请求内容
		System.out.println("请求地址 : " + request.getRequestURL().toString());
	}
	   
	@AfterReturning(returning = "ret", pointcut = "logPointCut()")// returning的值和doAfterReturning的参数名一致
	public void doAfterReturning(Object ret) throws Throwable {
		// 处理完请求，返回内容(返回值太复杂时，打印的是物理存储空间的地址)
	}

	@Around("logPointCut()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long startTime = System.currentTimeMillis();
	    Object ob = pjp.proceed();// ob 为方法的返回值
	    System.out.println("耗时 : " + (System.currentTimeMillis() - startTime));
	    return ob;
	}
}

