package app.spring.service

import app.spring.graphql.types.Book
import app.spring.graphql.types.BookInput
import app.spring.model.toDto
import app.spring.model.toEntity
import app.spring.repository.BookRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

interface BookService {
    fun getByUuid(uuid: String): Mono<Book>

    fun getAll(): Mono<List<Book>>

    fun save(book: BookInput): Mono<Book>
}

@Component
class BookServiceImpl(
    private val bookRepository: BookRepository,
) : BookService {
    override fun getByUuid(uuid: String): Mono<Book> {
        return bookRepository.findById(uuid)
            .map { it.toDto() }
    }

    override fun getAll(): Mono<List<Book>> {
        val booksFlux =
            bookRepository.findAll()
                .map { it.toDto() }

        return booksFlux.collectList()
    }

    override fun save(book: BookInput): Mono<Book> {
        val bookEntity = book.toEntity()
        return bookRepository.save(bookEntity)
            .map { it.toDto() }
    }
}
