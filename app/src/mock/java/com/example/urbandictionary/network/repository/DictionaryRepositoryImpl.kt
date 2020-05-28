package com.example.urbandictionary.network.repository

import com.example.urbandictionary.model.Definition
import com.example.urbandictionary.model.UrbanResponse
import com.example.urbandictionary.network.Webservices
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DictionaryRepositoryImpl(private val webservices: Webservices) : DictionaryRepository {

    override fun getDefinition(word: String): Single<UrbanResponse> {
        val definition1 = Definition(word, "definition 1", "example1", 1,
            2, "author", "2005-06-17T00:00:00.000Z")
        val definition2 = Definition(word, "definition 2", "example 2", 1,
            2, "author", "2005-06-17T00:00:00.000Z")
        val definitions = mutableListOf(definition1, definition2)
        val response = UrbanResponse(definitions)
        return Single.just(response)
    }

}