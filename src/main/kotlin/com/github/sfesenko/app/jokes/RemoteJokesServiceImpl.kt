package com.github.sfesenko.app.jokes

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

data class Joke(
    val id: Int,
    val type: String,
    val setup: String,
    val punchline: String,
)

/**
 * Interface required for tests, to replace actual service with stub
 */
interface RemoteJokesService {
    fun loadSingleJoke(): Joke?
    fun loadTenJokes(): Sequence<Joke>
}

@Service
class RemoteJokesServiceImpl(private val restTemplate: RestTemplate) : RemoteJokesService {

    override fun loadSingleJoke(): Joke? = restTemplate.getForObject(
        "https://official-joke-api.appspot.com/jokes/random",
        Joke::class.java
    )

    override fun loadTenJokes(): Sequence<Joke> = restTemplate.getForObject(
        "https://official-joke-api.appspot.com/jokes/ten",
        Array<Joke>::class.java
    )?.asSequence().orEmpty()
}