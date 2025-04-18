package com.github.sfesenko.app.jokes

class RemoteJokesServiceTest(val singleDelay: Long = 100, val batchDelay: Long = 200) : RemoteJokesService {
    override fun loadSingleJoke(): Joke? {
        // simulate blocking op
        Thread.sleep(singleDelay)
        return Joke(
            id = 1,
            type = "",
            setup = "",
            punchline = ""
        )
    }

    override fun loadTenJokes(): Sequence<Joke> {
        // simulate blocking op
        Thread.sleep(batchDelay)
        return (0..<10).map {
            Joke(
                id = it,
                type = "",
                setup = "",
                punchline = ""
            )
        }.asSequence()
    }
}
