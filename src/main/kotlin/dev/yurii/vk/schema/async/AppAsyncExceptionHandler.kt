package dev.yurii.vk.schema.async

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class AppAsyncExceptionHandler : AsyncUncaughtExceptionHandler {

    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        ex.printStackTrace()
    }
}
