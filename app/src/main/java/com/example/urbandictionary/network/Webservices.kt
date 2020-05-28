package com.example.urbandictionary.network

import com.example.urbandictionary.model.UrbanResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Webservices {
    @GET("define")
    fun getDefinition(@Query("term") input: String): Single<UrbanResponse>
}