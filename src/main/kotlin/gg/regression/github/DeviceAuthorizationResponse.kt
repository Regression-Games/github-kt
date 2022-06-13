package gg.regression.github

import com.fasterxml.jackson.annotation.JsonAlias

data class DeviceAuthorizationResponse(
    @JsonAlias("access_token")
    val accessToken: String,
    @JsonAlias("token_type")
    val tokenType: String,
    val scope: String
)
