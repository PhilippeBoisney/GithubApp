package io.github.philippeboisney.githubapp.model

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("login") val login: String,
                @SerializedName("avatar_url") val avatarUrl: String,
                @SerializedName("followers") val totalFollowers: Int,
                @SerializedName("public_repos")  var totalRepos: Int,
                @Transient var totalStars: Int,
                @Transient var followers: List<User>)