package dev.yurii.vk.polygon.api

import org.springframework.hateoas.ResourceSupport

data class UserGroupsResource(val group: com.vk.api.sdk.objects.groups.Group) : ResourceSupport() {
}