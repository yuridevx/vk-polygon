package dev.yurii.vk.polygon.vkoauth2.resolvers

import com.vk.api.sdk.client.actors.UserActor
import dev.yurii.vk.polygon.vkoauth2.services.AppUser
import org.springframework.core.MethodParameter
import org.springframework.lang.NonNull
import org.springframework.lang.Nullable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer


class UserActorResolver : HandlerMethodArgumentResolver {
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

        val clientRegistrationId = "vk_personal"

        val principal = SecurityContextHolder.getContext().authentication.principal
//
        when (principal) {
            is AppUser -> {
                return principal.getUserActor()
            }
            else -> {
                throw ClientAuthorizationRequiredException(clientRegistrationId)
            }
        }
    }


}