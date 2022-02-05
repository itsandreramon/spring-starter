package app.spring.repository

import app.spring.model.BookEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface BookRepository : ReactiveCrudRepository<BookEntity, String>
