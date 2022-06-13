package gg.regression.github

import com.fasterxml.jackson.annotation.JsonSetter

data class DeviceVerificationCodeRequest(
    @JsonSetter("client_id")
    val clientId: String,
    @JsonSetter("scope")
    val scope: String
)
