package io.github.philippeboisney.githubapp.di

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.github.philippeboisney.githubapp.api.UserService
import io.github.philippeboisney.githubapp.repository.UserRepository
import io.github.philippeboisney.githubapp.ui.user.search.SearchUserViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    factory<Interceptor> {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("API", it) })
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    factory { OkHttpClient.Builder().addInterceptor(get()).build() }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory{ get<Retrofit>().create(UserService::class.java) }
}