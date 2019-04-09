package dev.yurii.vk.polygon.api;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.groups.GroupsGetFilter;
import dev.yurii.vk.polygon.vkauth.VkOauth2User;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupsListController {

    @Autowired
    private VkApiClient vk;


    @RequestMapping("/groups")
    public Object user(@AuthenticationPrincipal VkOauth2User user) throws ClientException, ApiException {
        var groups = vk.groups().get(user.getUserActor()).filter(GroupsGetFilter.ADMIN).execute();
        return groups;
    }
}
