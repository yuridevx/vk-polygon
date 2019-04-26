package dev.yurii.vk.schema

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class VkPolygonApplication

fun main(args: Array<String>) {
    SpringApplication.run(VkPolygonApplication::class.java, *args)
}