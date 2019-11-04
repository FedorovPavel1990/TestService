package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Objects;

@Configuration
@EnableAsync
public class ExecutorConfig {

    @Autowired
    private Environment env;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Integer.parseInt(Objects.requireNonNull(env.getProperty("testservice.executors_count"))));
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("threadPoolTaskExecutor");
        executor.initialize();
        return executor;
    }

}