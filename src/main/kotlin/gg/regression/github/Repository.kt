package gg.regression.github

import com.fasterxml.jackson.annotation.JsonAlias
import java.time.LocalDateTime

data class Repository(
    val id: Int,
    val name: String,
    @JsonAlias("full_name")
    val fullName: String,
    val owner: Owner,
    val private: Boolean,
    val description: String?,
    @JsonAlias("html_url")
    val htmlUrl: String,
    @JsonAlias("default_branch")
    val defaultBranch: String,
    val visibility: String,
    @JsonAlias("pushed_at")
    val pushedAt: String,
    @JsonAlias("created_at")
    val createdAt: LocalDateTime,
    @JsonAlias("updated_at")
    val updatedAt: LocalDateTime,
)
