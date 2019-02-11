package io.github.philippeboisney.githubapp

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import io.github.philippeboisney.githubapp.di.appComponent
import org.koin.android.ext.android.startKoin

open class GithubApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        configureDi()
        LeakCanary.install(this)
    }

    // CONFIGURATION ---
    open fun configureDi() =
        startKoin(this, provideComponent())

    // PUBLIC API ---
    open fun provideComponent() = appComponent
}