package gbicc.irs.fbinterface.jpa.support.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 * 
 * @author songxubei
 * @version v1.0 2019年10月12日
 */
@Configuration
public class FbThreadExecutorConfig {
    /**
     * 配置线程池，可配置多个线程池，并制定名称
     *
     * @return
     */
    @Bean(name="fbTaskExecutor")
    public TaskExecutor fbTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        executor.setCorePoolSize(10);
        //设置最大线程数
        executor.setMaxPoolSize(10);
        //设置队列容量
        executor.setQueueCapacity(20);
        //设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        //设置线程名称前缀
        executor.setThreadNamePrefix("fbThread--------");
        //设置拒绝策略
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //初始化
        executor.initialize();
        return executor;
    }
    
    @Bean(name="ruleTaskExecutor")
    public TaskExecutor ruleTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        executor.setCorePoolSize(5);
        //设置最大线程数
        executor.setMaxPoolSize(5);
        //设置队列容量
        executor.setQueueCapacity(20);
        //设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        //设置线程名称前缀
        executor.setThreadNamePrefix("ruleThread--------");
        //设置拒绝策略
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //初始化
        executor.initialize();
        return executor;
    }
}
