package dev.yurii.vk.schema.async.tasks

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.httpclient.HttpTransportClient
import dev.yurii.vk.schema.persistence.entities.GroupToken
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class GroupConfiguration {

    @Async
    fun installGroupCallback(group: GroupToken) {
        val vk = VkApiClient(HttpTransportClient())
        val actor = group.toGroupActor()

        val groupFull = vk.groups()
                .getById(actor)
                .groupId(group.groupId.toString())
                .execute()[0]

        val cbs = vk.groups().getCallbackServers(actor).execute()

        for (server in cbs.items) {

        }
    }
}