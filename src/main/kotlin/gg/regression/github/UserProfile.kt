package gg.regression.github

import com.fasterxml.jackson.annotation.JsonAlias
import java.time.LocalDateTime

data class UserProfile(

    val login: String,

    val id: Int,

    @JsonAlias("node_id")
    val nodeId: String,

    @JsonAlias("avatar_url")
    val avatarUrl: String,

    @JsonAlias("gravatar_id")
    val gravatarId: String,

    val url: String,

    @JsonAlias("html_url")
    val htmlUrl: String,

    @JsonAlias("followers_url")
    val followersUrl: String,

    @JsonAlias("following_url")
    val followingUrl: String,

    @JsonAlias("gists_url")
    val gistsUrl: String,

    @JsonAlias("starred_url")
    val starredUrl: String,

    @JsonAlias("subscriptions_url")
    val subscriptionsUrl: String,

    @JsonAlias("organizations_url")
    val organizationsUrl: String,

    @JsonAlias("repos_url")
    val reposUrl: String,

    @JsonAlias("events_url")
    val eventsUrl: String,

    @JsonAlias("received_events_url")
    val receivedEventsUrl: String,

    val type: String,

    @JsonAlias("site_admin")
    val siteAdmin: Boolean,

    val name: String,

    val company: String,

    val blog: String,

    val location: String,

    val email: String,

    val hireable: Boolean? = false,

    val bio: String,

    @JsonAlias("twitter_username")
    val twitterUsername: String?,

    @JsonAlias("public_repos")
    val publicRepos: Int,

    @JsonAlias("public_gists")
    val publicGists: Int,

    val followers: Int,

    val following: Int,

    @JsonAlias("created_at")
    val createdAt: LocalDateTime,

    @JsonAlias("updated_at")
    val updatedAt: LocalDateTime,

    @JsonAlias("private_gists")
    val privateGists: Int,

    @JsonAlias("total_private_repos")
    val totalPrivateRepos: Int,

    @JsonAlias("owned_private_repos")
    val ownedPrivateRepos: Int,

    @JsonAlias("disk_usage")
    val diskUsage: Int,

    val collaborators: Int,

    @JsonAlias("two_factor_authentication")
    val twoFactorAuthentication: Boolean,

    val plan: Plan
)
