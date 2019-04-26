package dev.yurii.vk.schema.vkoauth2.web

import com.fasterxml.jackson.databind.ObjectMapper
import dev.yurii.vk.schema.vkoauth2.controllers.VKGroupAuthController
import dev.yurii.vk.schema.vkoauth2.data.GroupAuthRequiredException
import dev.yurii.vk.schema.vkoauth2.data.UserAuthRequiredException
import dev.yurii.vk.schema.vkoauth2.data.VKAuthErrorResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.rest.webmvc.support.BaseUriLinkBuilder
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.security.web.util.ThrowableAnalyzer
import org.springframework.web.filter.GenericFilterBean
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.jvm.javaMethod

class VKOauth2AuthenticationExceptionFilter : GenericFilterBean() {

    @Autowired
    private lateinit var mapper: ObjectMapper

    private var throwableAnalyzer = ThrowableAnalyzer()


    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request !is HttpServletRequest || response !is HttpServletResponse) {
            chain.doFilter(request, response)
            return
        }

        try {
            chain.doFilter(request, response)
        } catch (ex: Exception) {
            val causes = throwableAnalyzer.determineCauseChain(ex)

            val groupExceptions = throwableAnalyzer.getFirstThrowableOfType(
                    GroupAuthRequiredException::class.java, causes
            ) as GroupAuthRequiredException?

            val userException = throwableAnalyzer.getFirstThrowableOfType(
                    UserAuthRequiredException::class.java, causes
            ) as UserAuthRequiredException?

            if (userException != null) {
                userResponse(request, response)
                return
            }

            if (groupExceptions != null) {
                groupResponse(request, response, groupExceptions.groupId)
                return
            }
        }
    }

    private fun userResponse(request: HttpServletRequest, response: HttpServletResponse) {
        val resource = VKAuthErrorResource()
        val host = request.getHeader(HttpHeaders.HOST).split(":")
        val uri = UriComponentsBuilder
                .fromPath("/oauth2/authorization/vk_personal")
                .scheme(request.scheme)
                .host(host[0])
                .port(host[1])
                .query("")
                .build()
                .toUri()

        resource.add(BaseUriLinkBuilder.create(uri).withRel("connect"))

        mapper.writeValue(response.outputStream, resource)
    }

    private fun groupResponse(request: HttpServletRequest, response: HttpServletResponse, groupId: Int) {
        val resource = VKAuthErrorResource()

        resource.add(linkTo(VKGroupAuthController::redirectToGroupAuth.javaMethod, groupId).withRel("connect"))

        mapper.writeValue(response.outputStream, resource)
    }


}