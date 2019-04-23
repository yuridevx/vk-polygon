package dev.yurii.vk.polygon.vkoauth2.services

import dev.yurii.vk.polygon.vkoauth2.data.VKGroupAuthData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import javax.inject.Provider
import javax.servlet.http.HttpServletRequest

const val SESSION_ATTRIBUTE_NAME = "vk_group_state";

@Component
class GroupStateStorageImpl : GroupStateStorage {
    @Autowired
    private lateinit var requests: Provider<HttpServletRequest>;

    override fun saveRedirectData(data: VKGroupAuthData) {
        val request = requests.get()
        request.session.setAttribute(SESSION_ATTRIBUTE_NAME, data.copy())
    }


    override fun findRedirectData(state: String): VKGroupAuthData {
        val request = requests.get()
        val data = request.session.getAttribute(SESSION_ATTRIBUTE_NAME) as VKGroupAuthData?
                ?: throw AccessDeniedException("State parameter is not correct")

        return data
    }
}