package gg.regression.github

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.TimeZone

/**
 * An object that implements the functionality for the GitHub API.
 * Any function here can throw an UnauthorizedException if the GitHub API responds with a 401 error code.
 */
class GitHubClient(private val oauthToken: String) : HttpClient() {

    private val baseUrl = "https://api.github.com"

    private inline fun <reified T> getAuthed(url: String, headers: Map<String, String> = mapOf()) =
        this.get<T>(url, mutableMapOf("Authorization" to "token $oauthToken").apply { this.putAll(headers) })
    private inline fun <reified T> postAuthed(url: String, data: Any, headers: Map<String, String> = mapOf()) =
        this.post<T>(url, data, mutableMapOf("Authorization" to "token $oauthToken").apply { this.putAll(headers) })

    /**
     * Returns the user profile of the currently authenticated user.
     * Docs regarding this API can be found here: https://docs.github.com/en/rest/users/users#get-the-authenticated-user
     * @return The profile of the user
     */
    fun getLoggedInUser(): UserProfile {
        return getAuthed("$baseUrl/user")
    }

    /**
     * Returns a list of repositories for the authenticated user.
     * Docs regarding this API can be found here: https://docs.github.com/en/rest/repos/repos#list-repositories-for-the-authenticated-user
     */
    fun getRepositories(
        visibility: Visibility = Visibility.ALL,
        affiliation: List<Affiliation>? = listOf(Affiliation.COLLABORATOR, Affiliation.OWNER, Affiliation.ORGANIZATION_MEMBER),
        sortMode: RepositorySortMode? = null,
        sortAscending: Boolean? = null,
        resultsPerPage: Int? = null,
        pageNumber: Int? = null,
        since: LocalDateTime? = null,
        before: LocalDateTime? = null
    ): List<Repository> {
        val headers = mapOf("Accept" to "application/vnd.github.v3+json")
        val url = "$baseUrl/user/repos"
        val httpBuilder = url.toHttpUrlOrNull()!!.newBuilder()
        httpBuilder.addQueryParameter("visibility", visibility.name.lowercase())
        affiliation?.run {
            if (affiliation.isNotEmpty())
                httpBuilder.addQueryParameter("affiliation", this.joinToString(",") { it.name.lowercase() })
        }
        sortMode?.run {
            httpBuilder.addQueryParameter("sort", this.name.lowercase())
        }
        sortAscending?.run {
            httpBuilder.addQueryParameter("direction", if (this) "asc" else "desc")
        }
        resultsPerPage?.run {
            httpBuilder.addQueryParameter("per_page", this.toString())
        }
        pageNumber?.run {
            httpBuilder.addQueryParameter("page", this.toString())
        }
        // Note, I have not tested these yet, this is a good started task
        since?.run {
            val tz = TimeZone.getTimeZone("UTC")
            val df: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'") // Quoted "Z" to indicate UTC, no timezone offset
            df.timeZone = tz
            val nowAsISO = df.format(this)
            httpBuilder.addQueryParameter("since", nowAsISO)
        }
        before?.run {
            val tz = TimeZone.getTimeZone("UTC")
            val df: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'") // Quoted "Z" to indicate UTC, no timezone offset
            df.timeZone = tz
            val nowAsISO = df.format(this)
            httpBuilder.addQueryParameter("before", nowAsISO)
        }
        val queryUrl = httpBuilder.build().toString()
        println(queryUrl)
        return this.getAuthed(queryUrl, headers)
    }

    fun getBranches(
        owner: String,
        repo: String,
        protected: Boolean? = null,
        resultsPerPage: Int? = null,
        pageNumber: Int? = null
    ): List<Branch> {
        val headers = mapOf("Accept" to "application/vnd.github.v3+json")
        val url = "$baseUrl/repos/$owner/$repo/branches"
        val httpBuilder = url.toHttpUrlOrNull()!!.newBuilder()
        protected?.run {
            httpBuilder.addQueryParameter("protected", this.toString())
        }
        resultsPerPage?.run {
            httpBuilder.addQueryParameter("per_page", this.toString())
        }
        pageNumber?.run {
            httpBuilder.addQueryParameter("page", this.toString())
        }
        val queryUrl = httpBuilder.build().toString()
        return this.getAuthed(queryUrl, headers)
    }

    fun getCommits(
        owner: String,
        repo: String,
        shaOrBranch: String? = null,
        path: String? = null,
        author: String? = null,
        since: LocalDateTime? = null,
        until: LocalDateTime? = null,
        resultsPerPage: Int? = null,
        pageNumber: Int? = null,
    ): List<Commit> {
        val headers = mapOf("Accept" to "application/vnd.github.v3+json")
        val url = "$baseUrl/repos/$owner/$repo/commits"
        val httpBuilder = url.toHttpUrlOrNull()!!.newBuilder()
        shaOrBranch?.run {
            httpBuilder.addQueryParameter("sha", this)
        }
        path?.run {
            httpBuilder.addQueryParameter("path", this)
        }
        author?.run {
            httpBuilder.addQueryParameter("author", this)
        }
        since?.run {
            val tz = TimeZone.getTimeZone("UTC")
            val df: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'") // Quoted "Z" to indicate UTC, no timezone offset
            df.timeZone = tz
            val nowAsISO = df.format(this)
            httpBuilder.addQueryParameter("since", nowAsISO)
        }
        until?.run {
            val tz = TimeZone.getTimeZone("UTC")
            val df: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'") // Quoted "Z" to indicate UTC, no timezone offset
            df.timeZone = tz
            val nowAsISO = df.format(this)
            httpBuilder.addQueryParameter("until", nowAsISO)
        }
        resultsPerPage?.run {
            httpBuilder.addQueryParameter("per_page", this.toString())
        }
        pageNumber?.run {
            httpBuilder.addQueryParameter("page", this.toString())
        }
        val queryUrl = httpBuilder.build().toString()
        return this.getAuthed(queryUrl, headers)
    }

    fun listDirectory(
        owner: String,
        repo: String,
        path: String,
        ref: String? = null
    ): List<DirectoryItem> {
        val headers = mapOf("Accept" to "application/vnd.github.v3+json")
        val url = "$baseUrl/repos/$owner/$repo/contents/${path.trimStart('/')}"
        val httpBuilder = url.toHttpUrlOrNull()!!.newBuilder()
        ref?.run {
            httpBuilder.addQueryParameter("ref", this)
        }
        val queryUrl = httpBuilder.build().toString()
        return this.getAuthed(queryUrl, headers)
    }
}
