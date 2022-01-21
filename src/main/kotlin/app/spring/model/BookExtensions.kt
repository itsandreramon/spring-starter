package app.spring.model

import app.spring.graphql.types.Book
import app.spring.graphql.types.BookInput

fun BookInput.toEntity(): BookEntity {
    return BookEntity(
        title = this.title,
        author = this.author,
    )
}

fun BookEntity.toDto(): Book {
    return Book(
        uuid = this.uuid,
        author = this.author,
        title = this.title,
        created = this.created,
    )
}
