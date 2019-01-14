package io.github.philippeboisney.githubapp

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import io.github.philippeboisney.githubapp.di.appComponent
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module

open class GithubApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        configureDi()
        configureEmojiCompat()
    }

    // CONFIGURATION ---
    open fun configureDi() =
        startKoin(this, provideComponent())

    private fun configureEmojiCompat() {
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }

    // PUBLIC API ---
    open fun provideComponent() = appComponent
}