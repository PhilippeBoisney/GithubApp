package io.github.philippeboisney.githubapp.repository

import io.github.philippeboisney.githubapp.api.UserService
import io.github.philippeboisney.githubapp.model.User

class UserRepository(private val service: UserService) {

    private suspend fun search(query: String, page: Int, perPage: Int, sort: String) = service.search(query, page, perPage, sort).await()

    private suspend fun getDetail(login: String) = service.getDetail(login).await()

    private suspend fun getRepos(login: String) = service.getRepos(login).await()

    private suspend fun getFollowers(login: String) = service.getFollowers(login).await()

    suspend fun searchUsersWithPagination(query: String, page: Int, perPage: Int, sort: String): List<User> {
        if (query.isEmpty()) return listOf()

        val users = mutableListOf<User>()
        val request = search(query, page, perPage, sort) // Search by name
        request.items.forEach {
            val user = getDetail(it.login) // Fetch detail for each user
            val repositories = getRepos(user.login) // Fetch all repos for each user
            val followers = getFollowers(user.login) // Fetch all followers for each user

            user.totalStars =  repositories.map { it.numberStars }.sum()
            user.followers = if (followers.isNotEmpty()) followers else listOf()

            users.add(user)
        }
        return users
    }
}