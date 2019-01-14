package io.github.philippeboisney.githubapp.api

import io.github.philippeboisney.githubapp.BuildConfig
import io.github.philippeboisney.githubapp.model.Repository
import io.github.philippeboisney.githubapp.model.Result
import io.github.philippeboisney.githubapp.model.User
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("search/users")
    fun search(@Query("q") query: String,
               @Query("page") page: Int,
               @Query("per_page") perPage: Int,
               @Query("sort") sort: String,
               @Query("client_id") clientId: String = BuildConfig.GithubClientId,
               @Query("client_secret") clientSecret: String = BuildConfig.GithubClientSecret): Deferred<Result>

    @GET("users/{username}")
    fun getDetail(@Path("username") username: String,
                  @Query("client_id") clientId: String = BuildConfig.GithubClientId,
                  @Query("client_secret") clientSecret: String = BuildConfig.GithubClientSecret): Deferred<User>

    @GET("users/{username}/repos")
    fun getRepos(@Path("username") username: String,
                 @Query("client_id") clientId: String = BuildConfig.GithubClientId,
                 @Query("client_secret") clientSecret: String = BuildConfig.GithubClientSecret): Deferred<List<Repository>>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String,
                     @Query("per_page") perPage: Int = 2,
                     @Query("client_id") clientId: String = BuildConfig.GithubClientId,
                     @Query("client_secret") clientSecret: String = BuildConfig.GithubClientSecret): Deferred<List<User>>
}