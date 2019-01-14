package io.github.philippeboisney.githubapp.di

import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.platform.app.InstrumentationRegistry
import io.github.philippeboisney.githubapp.repository.UserRepository
import io.github.philippeboisney.githubapp.storage.SharedPrefsManager
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val storageModuleTest = module {
    single { SharedPrefsManager(InstrumentationRegistry.getInstrumentation().context) }
}