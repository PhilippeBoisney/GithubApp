package io.github.philippeboisney.githubapp.model

import io.github.philippeboisney.githubapp.api.UserService

object Filters {

    /**
     * Filters used by [UserService]
     * to sort "search" queries
     */
    enum class ResultSearchUsers(val value: String) {
        BY_REPOS("repositories"), BY_FOLLOWERS("followers"), BY_SCORE("score")
    }
}