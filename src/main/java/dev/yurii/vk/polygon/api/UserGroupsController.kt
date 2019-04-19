package dev.yurii.vk.polygon.api

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.queries.groups.GroupsGetFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.ExposesResourceFor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/groups")
@ExposesResourceFor(UserGroupsResource::class)
class UserGroupsController {

    @Autowired
    private lateinit var vk: VkApiClient

    @GetMapping
    fun getUserGroups(actor: UserActor): Any {
        val groups = vk.groups().getExtended(actor).filter(GroupsGetFilter.ADMIN).execute()
        return ResponseEntity.ok(groups)
    }
}
