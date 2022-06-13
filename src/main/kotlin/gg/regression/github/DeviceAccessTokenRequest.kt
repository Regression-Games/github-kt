package gg.regression.github

import com.fasterxml.jackson.annotation.JsonSetter

data class DeviceAccessTokenRequest(
    @JsonSetter("client_id")
    val clientId: String,
    @JsonSetter("device_code")
    val deviceCode: String,
    @JsonSetter("grant_type")
    val grantType: String
)
