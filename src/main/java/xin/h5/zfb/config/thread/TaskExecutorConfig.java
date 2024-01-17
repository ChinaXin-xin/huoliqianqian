package xin.h5.zfb.config.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@PropertySource("classpath:/thread.properties")
public class TaskExecutorConfig implements AsyncConfigurer{

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskExecutorConfig.class);

    @Value("${task.executor.thread.core_pool_size}")
    private int corePoolSize;
    @Value("${task.executor.thread.max_pool_size}")
    private int maxPoolSize;
    @Value("${task.executor.thread.queue_capacity}")
    private int queueCapacity;
    @Value("${task.executor.thread.name.prefix}")
    private String namePrefix;

    @Bean
    public Executor getAsyncExecutor() {
    	LOGGER.info("STAET TaskExecutorConfig");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(namePrefix);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
