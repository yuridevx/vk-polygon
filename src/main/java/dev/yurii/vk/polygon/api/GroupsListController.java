package dev.yurii.vk.polygon.api;

import com.vk.api.sdk.client.VkApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupsListController {

    @Autowired
    private VkApiClient vk;

}
