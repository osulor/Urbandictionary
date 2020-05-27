package com.example.urbandictionary.network

import com.example.urbandictionary.model.UrbanResponse
import com.example.urbandictionary.utils.Constants
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Webservices {
    // Provide retrofit interface through DI
    companion object {
        val instance: Webservices by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(Webservices::class.java)
        }
    }

    @GET("define")
    @Headers(Constants.HOST, Constants.API_KEY)
    fun getDefinition(@Query("term") input: String): Single<UrbanResponse>
}