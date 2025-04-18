package com.github.sfesenko.app.jokes

import kotlinx.coroutines.*
import org.springframework.stereotype.Service

@Service
class JokesService(val remoteJokesService: RemoteJokesService) {

    // val logger: Logger = LoggerFactory.getLogger(JokesService::class.java)

    @OptIn(ObsoleteCoroutinesApi::class)
    suspend fun getJokes(count: Int): Sequence<Joke> = coroutineScope {
        var remain = count
        val batch = mutableListOf<Deferred<Sequence<Joke>>>()
        val singles = mutableListOf<Deferred<Joke?>>()
        while (remain > 0) {
            when {
                /**
                 * can use different strategies for fetching < 10 jokes:
                 * - do multiple queries for single joke
                 * - do single batch query for 10 jokes and discard rest
                 */

                remain == 1 -> {
                    remain -= 1
                    singles += async(Dispatchers.IO) {
                        remoteJokesService.loadSingleJoke()
                    }

                }

                else -> {
                    remain -= 10
                    batch += async(Dispatchers.IO) {
                        remoteJokesService.loadTenJokes().asSequence()
                    }
                }
            }
        }
        val result = mutableListOf<Joke>()
        batch.awaitAll().flatMapTo(result) { it }
        singles.awaitAll().mapNotNullTo(result) { it }
        result.asSequence().take(count)
    }
}
