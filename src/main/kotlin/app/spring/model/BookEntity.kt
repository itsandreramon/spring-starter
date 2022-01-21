package app.spring.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Document(collection = "books")
data class BookEntity(
    @Id
    val uuid: String = UUID.randomUUID().toString(),
    @Indexed
    val title: String,
    val author: String,
    val created: String = Instant.now().toString(),
)
