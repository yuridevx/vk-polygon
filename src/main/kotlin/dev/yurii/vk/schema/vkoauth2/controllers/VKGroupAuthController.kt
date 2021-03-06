package dev.yurii.vk.schema.vkoauth2.controllers

import com.vk.api.sdk.client.VkApiClient
import dev.yurii.vk.schema.api.IndexController
import dev.yurii.vk.schema.relational.services.AppAuthService
import dev.yurii.vk.schema.vkoauth2.data.VKGroupAuthData
import dev.yurii.vk.schema.vkoauth2.services.GroupStateStorage
import dev.yurii.vk.schema.vkoauth2.services.VkUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.view.RedirectView
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
import kotlin.reflect.jvm.javaMethod

@Controller
@RequestMapping("/connect/group")
class VKGroupAuthController {

    @Autowired
    private lateinit var vk: VkApiClient

    @Autowired
    private lateinit var registration: ClientRegistration

    @Autowired
    private lateinit var authService: AppAuthService

    @Autowired
    private lateinit var userService: VkUserService

    @Autowired
    private lateinit var stateStorage: GroupStateStorage

    private val stateGenerator = Base64StringKeyGenerator(Base64.getUrlEncoder())

    @GetMapping
    @RequestMapping("/authenticate")
    fun authenticateGroup(
            @RequestParam("code") code: String,
            @RequestParam("state") state: String
    ): RedirectView {
        val user = userService.ensureUserAuthenticated()
        val data = stateStorage.findRedirectData(state)

        val response = vk.oauth()
                .groupAuthorizationCodeFlow(
                        registration.clientId.toInt(),
                        registration.clientSecret,
                        data!!.redirectUri,
                        code
                ).execute()

        val token = response.accessTokens[data.groupId]!!

        authService.getOrCreateGroupToken(user, token, data.groupId)

        return RedirectView(linkTo(IndexController::index.javaMethod).toUri().toString())
    }

    @GetMapping
    @RequestMapping("/redirect/{groupId}")
    fun redirectToGroupAuth(@PathVariable groupId: Int): Any {
        userService.ensureUserAuthenticated()

        val state = stateGenerator.generateKey()
        val redirectUri = linkTo(VKGroupAuthController::authenticateGroup.javaMethod).toUri().toString()

        val updatedData = VKGroupAuthData(
                state = state,
                redirectUri = redirectUri,
                groupId = groupId
        )

        stateStorage.saveRedirectData(updatedData)

        val authUrl = UriComponentsBuilder.fromHttpUrl(registration.providerDetails.authorizationUri)
                .queryParam("group_ids", updatedData.groupId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("client_id", registration.clientId)
                .queryParam("scope", "manage")
                .queryParam("response_type", "code")
                .queryParam("state", state)
                .build()
                .toUriString()

        return RedirectView(authUrl)
    }
}

