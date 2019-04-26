package dev.yurii.vk.schema.vkoauth2.services

import dev.yurii.vk.schema.vkoauth2.data.VKGroupAuthData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import javax.inject.Provider
import javax.servlet.http.HttpServletRequest


@Component
class GroupStateStorageImpl : GroupStateStorage {
    val SESSION_ATTRIBUTE_NAME = "vk_group_state"

    @Autowired
    private lateinit var requests: Provider<HttpServletRequest>

    override fun saveRedirectData(data: VKGroupAuthData) {
        val request = requests.get()
        request.session.setAttribute(SESSION_ATTRIBUTE_NAME, data.copy())
    }


    override fun findRedirectData(state: String): VKGroupAuthData {
        val request = requests.get()

        return request.session.getAttribute(SESSION_ATTRIBUTE_NAME) as VKGroupAuthData?
                ?: throw AccessDeniedException("State parameter is not correct")
    }
}