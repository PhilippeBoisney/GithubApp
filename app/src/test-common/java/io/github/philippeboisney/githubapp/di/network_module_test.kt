package io.github.philippeboisney.githubapp.di

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.github.philippeboisney.githubapp.api.UserService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun configureNetworkModuleForTest(baseApi: String) = module {

    factory<Interceptor> {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { println("API: $it") })
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    factory { OkHttpClient.Builder().addInterceptor(get()).build() }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(baseApi)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory{ get<Retrofit>().create(UserService::class.java) }
}