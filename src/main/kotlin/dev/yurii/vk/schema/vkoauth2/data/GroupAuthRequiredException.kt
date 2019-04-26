package dev.yurii.vk.schema.vkoauth2.data

class GroupAuthRequiredException(
        val groupId: Int
) : Exception("VK group $groupId authentication have to be performed")