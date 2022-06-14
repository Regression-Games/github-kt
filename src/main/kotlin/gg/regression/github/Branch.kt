package gg.regression.github

data class Branch(
    val name: String,
    val commit: BranchCommit,
    val protected: Boolean,
)
