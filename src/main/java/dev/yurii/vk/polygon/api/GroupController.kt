package dev.yurii.vk.polygon.api

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.queries.groups.GroupsGetFilter
import dev.yurii.vk.polygon.vkoauth2.services.AppVkAuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors
import kotlin.reflect.jvm.javaMethod

@RestController
@RequestMapping("/api/groups")
class GroupController {

    @Autowired
    private lateinit var vk: VkApiClient

    @Autowired
    private lateinit var auth: AppVkAuthService

    @GetMapping
    fun getUserGroups(actor: UserActor): ResponseEntity<Resources<GroupResource>> {
        val groups = vk.groups().getExtended(actor).filter(GroupsGetFilter.ADMIN).execute().items

        val resourceList = groups
                .stream()
                .map { g -> GroupResource(g) }
                .peek { r -> r.links.add(linkTo(GroupController::getGroupDetails.javaMethod, r.group.id).withRel("status")) }
                .collect(Collectors.toList())

        return ResponseEntity.ok(Resources(resourceList))
    }

    @GetMapping
    @RequestMapping("/{groupId}/status")
    fun getGroupDetails(@PathVariable groupId: Int): Any {

        val actor = auth.ensureGroupAuthenticated(groupId).toGroupActor()
        val settings = vk.groups().getCallbackServers(actor).execute()

        return settings
    }
}
