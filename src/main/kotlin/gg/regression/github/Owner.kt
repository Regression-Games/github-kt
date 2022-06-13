package gg.regression.github

import com.fasterxml.jackson.annotation.JsonAlias

data class Owner(
    val login: String,
    val id: Int,
    @JsonAlias("node_id")
    val nodeId: String,

)
