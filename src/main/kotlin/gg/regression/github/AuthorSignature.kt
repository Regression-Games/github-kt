package gg.regression.github

import java.time.LocalDateTime

data class AuthorSignature(
    val name: String,
    val email: String,
    val date: LocalDateTime
)
