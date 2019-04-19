package dev.yurii.vk.polygon

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class VkPolygonApplication {


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(VkPolygonApplication::class.java, *args)
        }
    }
}
