package com.example.urbandictionary.network.repository

import com.example.urbandictionary.model.UrbanResponse
import io.reactivex.Single

interface DictionaryRepository {
    fun getDefinition(word: String) : Single<UrbanResponse>
}