package com.github.sfesenko.app

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
class JokesConfiguration {
    @Bean
    fun restTemplate(): RestTemplate {
        val builder = RestTemplateBuilder()
        return builder.build()
    }
}