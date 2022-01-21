package app.spring.service

import app.spring.graphql.types.Book
import app.spring.graphql.types.BookInput
import app.spring.model.BookEntity
import app.spring.repository.BookRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class BookServiceTest {
    private val bookRepository = mockk<BookRepository>()
    private var bookService: BookService? = null

    @BeforeEach
    fun setUp() {
        bookService = BookServiceImpl(bookRepository)
    }

    @AfterEach
    fun tearDown() {
        bookService = null
    }

    @Test
    fun test_entity_conversion() {
        val created = "2022-01-21T14:48:19.374212Z"
        val uuid = "eefc96ef-00b1-4a5a-9e7f-489691d04e09"

        val inputTitle = "Example Title"
        val inputAuthor = "Example Author"

        val input =
            BookInput(
                author = inputAuthor,
                title = inputAuthor,
            )

        every {
            bookRepository.save(any())
        } returns
            Mono.just(
                BookEntity(
                    title = inputTitle,
                    author = inputAuthor,
                    uuid = uuid,
                    created = created,
                ),
            )

        val expected =
            Book(
                author = inputAuthor,
                title = inputTitle,
                uuid = uuid,
                created = created,
            )

        val book = bookService!!.save(input)

        StepVerifier.create(book)
            .expectNext(expected)
            .verifyComplete()
    }
}
