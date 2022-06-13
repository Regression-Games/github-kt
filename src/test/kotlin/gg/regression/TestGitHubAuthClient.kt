package gg.regression

import gg.regression.github.GitHubAuthClient
import gg.regression.github.exceptions.DeviceFlowAuthorizationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith

class TestGitHubAuthClient : FeatureSpec({

    feature("device verification request") {

        scenario("should get a device code when valid") {
            val exampleResponse = """
                {
                  "device_code": "3584d83530557fdd1f46af8289938c8ef79f9dc5",
                  "user_code": "WDJB-MJHT",
                  "verification_uri": "https://github.com/login/device",
                  "expires_in": 900,
                  "interval": 5
                }
            """.trimIndent()
            val client = GitHubAuthClient("")
            client.httpClient = getMockedClient(200, exampleResponse)
            val deviceVerificationCode = client.requestDeviceVerification("")
            deviceVerificationCode.deviceCode shouldBe "3584d83530557fdd1f46af8289938c8ef79f9dc5"
            deviceVerificationCode.userCode shouldBe "WDJB-MJHT"
            deviceVerificationCode.verificationUri shouldBe "https://github.com/login/device"
            deviceVerificationCode.expiresIn shouldBe 900
            deviceVerificationCode.interval shouldBe 5
        }

        scenario("should throw error when invalid client id") {
            val exampleResponse = """
                {"error":"Not Found"}
            """.trimIndent()
            val client = GitHubAuthClient("")
            client.httpClient = getMockedClient(404, exampleResponse)
            val exception = shouldThrow<DeviceFlowAuthorizationException> {
                client.requestDeviceVerification("")
            }
            exception.error shouldBe "Not Found"
        }
    }

    feature("poll for user authorization") {

        scenario("should get an access token when authorized") {
            val exampleResponse = """
                {
                    "access_token": "gho_16C7e42F292c6912E7710c838347Ae178B4a",
                    "token_type": "bearer",
                    "scope": "repo,gist"
                }
            """.trimIndent()
            val client = GitHubAuthClient("")
            client.httpClient = getMockedClient(200, exampleResponse)
            val authResponse = client.pollForAuthorization("example device code")
            authResponse.accessToken shouldBe "gho_16C7e42F292c6912E7710c838347Ae178B4a"
            authResponse.tokenType shouldBe "bearer"
            authResponse.scope shouldBe "repo,gist"
        }

        scenario("should throw error when not authorized yet") {
            val exampleResponse = """
                {
                    "error":"authorization_pending",
                    "error_description":"The authorization request is still pending.",
                    "error_uri":"https://docs.github.com/developers/apps/authorizing-oauth-apps#error-codes-for-the-device-flow"
                }
            """.trimIndent()
            val client = GitHubAuthClient("")
            client.httpClient = getMockedClient(500, exampleResponse)
            val exception = shouldThrow<DeviceFlowAuthorizationException> {
                client.pollForAuthorization("sample device code")
            }
            exception.error shouldBe "authorization_pending"
            exception.errorDescription shouldBe "The authorization request is still pending."
            exception.errorUri shouldStartWith "https://docs"
        }
    }
})
