package dev.yurii.vk.schema.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CallbackUrlServiceImpl : UrlService {

    @Value("\${platform.vk.callback.origin}")
    private lateinit var callbackOrigin: String

    @Value("\${platform.vk.app.origin}")
    private lateinit var appOrigin: String

    override fun getCallbackServerUrl(): String {
        return callbackOrigin + ""
    }

    override fun isCallbackServerUrl(url: String): Boolean {
        return true
    }

    override fun getSelfUrl(path: String): String {
        return appOrigin
    }
}