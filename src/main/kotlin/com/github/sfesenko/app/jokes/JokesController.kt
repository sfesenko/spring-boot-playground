package com.github.sfesenko.app.jokes

import org.hibernate.validator.constraints.Range
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class JokesController(val service: JokesService) {

    @GetMapping("/jokes")
    suspend fun jokes(
        @RequestParam @Range(max = 100, message = "{jokes.max_limit}") count: Int = 5,
    ): ResponseEntity<Sequence<Joke>> {

        return ResponseEntity.ok(service.getJokes(count))
    }
}
