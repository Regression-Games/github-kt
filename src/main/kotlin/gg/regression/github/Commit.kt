package gg.regression.github

import com.fasterxml.jackson.annotation.JsonAlias

data class Commit(
    val url: String,
    val sha: String,
    @JsonAlias("node_id")
    val nodeId: String,
    @JsonAlias("html_url")
    val htmlUrl: String,
    @JsonAlias("comments_url")
    val commentsUrl: String,
    val commit: CommitDetail,
    val author: CommitAuthor,
    val committer: CommitAuthor,
    val parents: List<Tree>
)
