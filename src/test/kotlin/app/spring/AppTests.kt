package app.spring

import app.spring.fetcher.BookFetcher
import app.spring.graphql.client.SaveBookGraphQLQuery
import app.spring.graphql.client.SaveBookProjectionRoot
import app.spring.graphql.types.BookInput
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import com.netflix.graphql.dgs.reactive.DgsReactiveQueryExecutor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.test.StepVerifier

@ComponentScan
@EnableAutoConfiguration
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    classes = [
        DgsAutoConfiguration::class,
        BookFetcher::class,
    ],
)
@Testcontainers
class AppTests {
    @Autowired lateinit var queryExecutor: DgsReactiveQueryExecutor

    @Test
    fun test_save_book() {
        val input =
            BookInput(
                title = "Harry Potter and the Philosopher's Stone",
                author = "J. K. Rowling",
            )

        val request =
            GraphQLQueryRequest(
                query =
                    SaveBookGraphQLQuery.Builder()
                        .book(input)
                        .build(),
                projection =
                    SaveBookProjectionRoot<Nothing, Nothing>()
                        .title(),
            )

        val response =
            queryExecutor.executeAndExtractJsonPath<String>(
                request.serialize(),
                "data.saveBook.title",
            )

        StepVerifier.create(response)
            .expectNext("Harry Potter and the Philosopher's Stone")
            .verifyComplete()
    }

    companion object {
        @Container
        val mongo: MongoDBContainer = MongoDBContainer("mongo")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl)
        }
    }
}
