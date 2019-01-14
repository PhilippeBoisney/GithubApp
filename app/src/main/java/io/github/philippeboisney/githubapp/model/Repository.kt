package io.github.philippeboisney.githubapp.model

import com.google.gson.annotations.SerializedName

data class Repository(@SerializedName("stargazers_count") val numberStars: Int)