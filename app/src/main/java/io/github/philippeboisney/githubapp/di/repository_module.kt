package io.github.philippeboisney.githubapp.di

import io.github.philippeboisney.githubapp.repository.UserRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    factory { UserRepository(get()) }
}