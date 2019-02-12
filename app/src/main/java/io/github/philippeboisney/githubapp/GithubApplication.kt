package io.github.philippeboisney.githubapp

import android.app.Application
import io.github.philippeboisney.githubapp.di.appComponent
import org.koin.android.ext.android.startKoin

open class GithubApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    // CONFIGURATION ---
    open fun configureDi() =
        startKoin(this, provideComponent())

    // PUBLIC API ---
    open fun provideComponent() = appComponent
}