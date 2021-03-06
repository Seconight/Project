package com.Attendance.student_sign_demo.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class ExecutorConfig {
    private static Logger logger = LogManager.getLogger(ExecutorConfig.class.getName());
    // 声明一个线程池(并指定线程池的名字)
    @Bean
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //配置核心线程数
        executor.setCorePoolSize(5);
    //配置最大线程数
        executor.setMaxPoolSize(10);
    //配置队列大小
        executor.setQueueCapacity(20);
    //  executor.setAwaitTerminationSeconds(2);
    //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("thread-");
    // rejection-policy：当pool已经达到max size的时候，如何处理新任务
    // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler(){
            @Override public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //r 就是请求的任务, executor 就是当前线程池
                System.out.println(r + " is discarding..");
            } });
    //执行初始化
        executor.initialize();
        return executor;
    }
}
