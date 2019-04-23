package dev.yurii.vk.polygon.vkoauth2.data

data class VKGroupAuthData(
        val state: String,
        val redirectUri: String,
        val groupId: Int
)