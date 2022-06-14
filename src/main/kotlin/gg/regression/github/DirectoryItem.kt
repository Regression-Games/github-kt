package gg.regression.github

import com.fasterxml.jackson.annotation.JsonAlias

data class DirectoryItem(
    val type: String,
    val size: Long,
    val name: String,
    val path: String,
    val sha: String,
    val url: String,
    @JsonAlias("git_url")
    val gitUrl: String,
    @JsonAlias("html_url")
    val htmlUrl: String,
    @JsonAlias("download_url")
    val downloadUrl: String?
)
