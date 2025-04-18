package com.github.sfesenko.app.jokes

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.time.measureTimedValue

class JokesServiceTest {

    val remoteService = RemoteJokesServiceTest()
    val service = JokesService(remoteService)

    /**
     * All requests must go in parallel,
     * so execution should take at most maxOf(SINGLE_REQ_TIME, TEN_REQ_TIME) + DELTA
     * use DELTA as 100ms
     */
    @ParameterizedTest
    @CsvSource(
        "      1| 200",
        "      9| 300",
        "    500| 300",
        "    100| 300",
        delimiter = '|'
    )
    fun timeOutTests(count: Int, expectedTime: Long) {
        val result = measureTimedValue {
            runBlocking { service.getJokes(count) }
        }
        Assertions.assertEquals(count, result.value.count())
        val time = result.duration
        Assertions.assertTrue(time.inWholeMilliseconds < expectedTime) {
            "response should be within ${expectedTime}ms, but got $time"
        }
    }
}