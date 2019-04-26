package dev.yurii.vk.schema.services

interface UrlService {
    fun getCallbackServerUrl(): String
    fun isCallbackServerUrl(url: String): Boolean
    fun getSelfUrl(path: String): String
}