package io.github.philippeboisney.githubapp.di

import io.github.philippeboisney.githubapp.di.configureNetworkModuleForTest
import io.github.philippeboisney.githubapp.di.repositoryModule
import io.github.philippeboisney.githubapp.di.viewModelModule

fun configureAppComponent(baseApi: String)
        = listOf(
    configureNetworkModuleForTest(baseApi),
    viewModelModule,
    repositoryModule
)