package gg.regression.github.exceptions

import com.fasterxml.jackson.annotation.JsonAlias

data class DeviceFlowAuthorizationException(
    val error: String?,
    @JsonAlias("error_description")
    val errorDescription: String?,
    @JsonAlias("error_uri")
    val errorUri: String?,
    val interval: Int?
) : Exception()

// TODO: Eventually we might make error an enum from
//       https://docs.github.com/en/developers/apps/building-oauth-apps/authorizing-oauth-apps#error-codes-for-the-device-flow
