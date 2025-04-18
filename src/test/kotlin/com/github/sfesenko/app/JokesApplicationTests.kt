package com.github.sfesenko.app

import com.github.sfesenko.app.jokes.Joke
import com.github.sfesenko.app.jokes.RemoteJokesService
import com.github.sfesenko.app.jokes.RemoteJokesServiceTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.bean.override.convention.TestBean
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringJUnitConfig
class JokesApplicationTests {

    /// replace RemoteJokesService with stub

    @TestBean
    lateinit var remoteJokesService: RemoteJokesService

    companion object {
        @JvmStatic
        fun remoteJokesService(): RemoteJokesService {
            return RemoteJokesServiceTest()
        }
    }

    @LocalServerPort
    private var port = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    // Tests

    @ParameterizedTest
    @CsvSource(
        "0",
        "1",
        "<null>",
        "100",
        nullValues = ["<null>"]
    )
    fun countedJokes(count: Int?) {
        val url = if (count == null) {
            "http://localhost:$port/jokes"
        } else {
            "http://localhost:$port/jokes?count=$count"
        }
        val rsp = restTemplate.getForEntity(
            url,
            Array<Joke>::class.java
        )
        val expectedCount = count ?: 5
        Assertions.assertEquals(HttpStatus.OK, rsp.statusCode)
        Assertions.assertEquals(expectedCount, rsp.body?.size)
    }

    @Test
    fun validationFailed() {
        val rsp = restTemplate.getForEntity(
            "http://localhost:$port/jokes?count=101",
            Map::class.java
        )
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, rsp.statusCode)
        Assertions.assertEquals("No more than 100 jokes", rsp.body?.get("message"))
    }
}
