package dev.yurii.vk.polygon.api

import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class IndexController {


    @GetMapping
    fun index(): ResourceSupport {
        val links = ResourceSupport()
        links.add(ControllerLinkBuilder.linkTo(GroupController::class.java).withRel("groups"))
        return links
    }
}
