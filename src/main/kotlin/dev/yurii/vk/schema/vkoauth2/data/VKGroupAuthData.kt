package dev.yurii.vk.schema.vkoauth2.data

data class VKGroupAuthData(
        val state: String,
        val redirectUri: String,
        val groupId: Int
)