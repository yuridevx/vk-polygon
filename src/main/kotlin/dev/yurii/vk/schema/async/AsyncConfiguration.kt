package dev.yurii.vk.schema.async

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Configuration
@EnableAsync
class AsyncConfiguration : AsyncConfigurer {

    @Bean
    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return AppAsyncExceptionHandler()
    }

    @Bean
    override fun getAsyncExecutor(): Executor? {
        return Executors.newWorkStealingPool(50)
    }
}
