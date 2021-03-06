package dev.yurii.vk.schema.vkoauth2.services

import dev.yurii.vk.schema.vkoauth2.data.VKGroupAuthData

interface GroupStateStorage {
    fun saveRedirectData(data: VKGroupAuthData)
    fun findRedirectData(state: String): VKGroupAuthData?
}