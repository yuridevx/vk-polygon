package dev.yurii.vk.polygon.vkoauth2.exceptions

class GroupAuthRequiredException(
        val groupId: Int
) : Exception("VK group $groupId authentication have to be performed")