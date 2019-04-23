package dev.yurii.vk.polygon.api

import com.vk.api.sdk.objects.groups.GroupFull
import org.springframework.hateoas.ResourceSupport

data class GroupResource(
        val group: GroupFull
) : ResourceSupport() {
}