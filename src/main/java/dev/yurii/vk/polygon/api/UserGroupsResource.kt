package dev.yurii.vk.polygon.api

import com.vk.api.sdk.objects.groups.GroupFull
import org.springframework.hateoas.ResourceSupport

data class UserGroupsResource(
        val group: List<GroupFull>
) : ResourceSupport()