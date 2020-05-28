package com.example.urbandictionary.di

import com.example.urbandictionary.di.BaseApplication.DatasourceProperties.API_KEY_PROPERTY
import com.example.urbandictionary.di.BaseApplication.DatasourceProperties.BASE_URL_PROPERTY
import com.example.urbandictionary.di.BaseApplication.DatasourceProperties.HOST_PROPERTY
import com.example.urbandictionary.network.Webservices
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

val networkModule = module {
    factory { provideOkHttpClient(getProperty(API_KEY_PROPERTY), getProperty(HOST_PROPERTY)) }
    factory { provideApi(get()) }
    single { provideRetrofit(get(), getProperty(BASE_URL_PROPERTY)) }
}

fun provideApi(retrofit: Retrofit): Webservices = retrofit.create(Webservices::class.java)

fun provideOkHttpClient(apiKey: String, host: String): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .connectTimeout(15L, TimeUnit.SECONDS)
        .readTimeout(1L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Request-Type", "Android")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-rapidapi-host", host)
                    .addHeader("x-rapidapi-key", apiKey)
                val request = requestBuilder.build()
                return chain.proceed(request)
            }
        }).build()
}

fun provideRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}