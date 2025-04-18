package com.github.sfesenko.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class JokesApplication


fun main(args: Array<String>) {
    runApplication<JokesApplication>(*args)
}

