package gg.regression.github

import com.fasterxml.jackson.annotation.JsonAlias

data class DeviceVerificationCode(
    @JsonAlias("device_code")
    val deviceCode: String,
    @JsonAlias("user_code")
    val userCode: String,
    @JsonAlias("verification_uri")
    val verificationUri: String,
    @JsonAlias("expires_in")
    val expiresIn: Int,
    val interval: Int
)
