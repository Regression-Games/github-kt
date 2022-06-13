package gg.regression.github

import gg.regression.github.exceptions.DeviceFlowAuthorizationException
import gg.regression.github.exceptions.HttpClientException

/**
 * An object that implements the authentication flows for GitHub personal access tokens.
 * Any function here can throw an UnauthorizedException if the GitHub API responds with a 401 error code.
 */
class GitHubAuthClient(private val clientId: String) : HttpClient() {

    /**
     * Requests a user verification code and verification URL that can be used to prompt the user to authenticate.
     * The given device code can be used within pollForAuthorization(), but you should not poll faster than the given
     * interval. Docs for this function can be found at the following link:
     * https://docs.github.com/en/developers/apps/building-oauth-apps/authorizing-oauth-apps#step-1-app-requests-the-device-and-user-verification-codes-from-github
     * @param scope The scopes to request for this authentication. Can be left blank.
     * @return A response that has the device code and follow-up URL to use to authenticate
     * @throws DeviceFlowAuthorizationException if the request fails for some reason.
     */
    @Throws(DeviceFlowAuthorizationException::class)
    fun requestDeviceVerification(scope: String = ""): DeviceVerificationCode {
        try {
            return this.post(
                "https://github.com/login/device/code",
                DeviceVerificationCodeRequest(clientId, scope),
                mapOf("Accept" to "application/json")
            )
        } catch (httpException: HttpClientException) {
            throw httpException.toObject<DeviceFlowAuthorizationException>()
        }
    }

    /**
     * Pings the GitHub API to determine if the given device auth instance (defined by the device code) has been
     * authorized. Docs for this function can be found at the following link:
     * https://docs.github.com/en/developers/apps/building-oauth-apps/authorizing-oauth-apps#step-3-app-polls-github-to-check-if-the-user-authorized-the-device
     * @param deviceCode The device code generated from requestDeviceVerification()
     * @return The authorization information with an access token if the user is now authorized
     * @throws DeviceFlowAuthorizationException if an error occurs, such as the user is not yet authenticated (i.e.
     *         error will be `authorization_pending`), or if the polling is happening too fast (i.e. error will be
     *         `slow_down). All codes can be found at the following link:
     *         https://docs.github.com/en/developers/apps/building-oauth-apps/authorizing-oauth-apps#rate-limits-for-the-device-flow
     */
    @Throws(DeviceFlowAuthorizationException::class)
    fun pollForAuthorization(deviceCode: String): DeviceAuthorizationResponse {
        try {
            return this.post(
                "https://github.com/login/oauth/access_token",
                DeviceAccessTokenRequest(clientId, deviceCode, "urn:ietf:params:oauth:grant-type:device_code"),
                mapOf("Accept" to "application/json")
            )
        } catch (httpException: HttpClientException) {
            throw httpException.toObject<DeviceFlowAuthorizationException>()
        }
    }
}
