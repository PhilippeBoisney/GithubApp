package io.github.philippeboisney.githubapp.di

import io.github.philippeboisney.githubapp.repository.UserRepository
import io.github.philippeboisney.githubapp.storage.SharedPrefsManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val storageModule = module {
    single { SharedPrefsManager(androidContext()) }
}