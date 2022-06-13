package gg.regression.github

/**
 * An object that implements the functionality for the GitHub API.
 * Any function here can throw an UnauthorizedException if the GitHub API responds with a 401 error code.
 */
class GitHubClient(private val oauthToken: String) : HttpClient() {

    private val baseUrl = "https://api.github.com"

    private inline fun <reified T> getAuthed(url: String) = this.get<T>(url, mapOf("Authorization" to "token $oauthToken"))

    /**
     * Returns the user profile of the currently authenticated user.
     * Docs regarding this API can be found here: https://docs.github.com/en/rest/users/users#get-the-authenticated-user
     * @return The profile of the user
     */
    fun getLoggedInUser(): UserProfile {
        return getAuthed("$baseUrl/user")
    }
}
