package gg.regression.github

import com.fasterxml.jackson.annotation.JsonAlias

data class CommitDetail(
    val url: String,
    val author: AuthorSignature,
    val committer: AuthorSignature,
    val message: String,
    val tree: Tree,
    @JsonAlias("comment_count")
    val commentCount: Int,
    val verification: Verification
)
