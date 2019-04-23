package dev.yurii.vk.polygon.vkoauth2.web

import com.vk.api.sdk.client.actors.UserActor
import dev.yurii.vk.polygon.vkoauth2.services.AppVkAuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.MethodParameter
import org.springframework.lang.NonNull
import org.springframework.lang.Nullable
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer


class UserActorResolver : HandlerMethodArgumentResolver {

    @Autowired
    private lateinit var authService: AppVkAuthService

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val parameterType = parameter.parameterType
        return UserActor::class.java.isAssignableFrom(parameterType)
    }

    @NonNull
    @Throws(Exception::class)
    override fun resolveArgument(parameter: MethodParameter,
                                 @Nullable mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest,
                                 @Nullable binderFactory: WebDataBinderFactory?): Any? {
        return authService.ensureUserAuthenticated().toUserActor()
    }


}