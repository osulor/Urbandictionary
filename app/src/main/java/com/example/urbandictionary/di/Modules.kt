package com.example.urbandictionary.di

import com.example.urbandictionary.network.repository.DictionaryRepository
import com.example.urbandictionary.network.repository.DictionaryRepositoryImpl
import com.example.urbandictionary.viewmodel.DictionaryViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { DictionaryViewModel(get()) }
}

val repositoryModule = module {
    factory { DictionaryRepositoryImpl(get()) as DictionaryRepository}
}