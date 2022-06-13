package gg.regression.github

import com.fasterxml.jackson.annotation.JsonAlias

data class Plan(
    val name: String,
    val space: Int,
    @JsonAlias("private_repos")
    val privateRepos: Int,
    val collaborators: Int
)
