package com.example.urbandictionary.network.repository

import com.example.urbandictionary.model.UrbanResponse
import com.example.urbandictionary.network.Webservices
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DictionaryRepositoryImpl(private val webservices: Webservices) : DictionaryRepository {
    override fun getDefinition(word: String): Single<UrbanResponse> {
       return webservices.getDefinition(word)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}