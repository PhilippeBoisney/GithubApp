package io.github.philippeboisney.githubapp.base

import android.app.Application
import io.github.philippeboisney.githubapp.GithubApplication
import org.koin.dsl.module.Module

/**
 * We use a separate [Application] for tests to prevent initializing release modules.
 * On the contrary, we will provide inside our tests custom [Module] directly.
 */
class TIBaseApplication : GithubApplication() {
    override fun provideComponent() = listOf<Module>()
}