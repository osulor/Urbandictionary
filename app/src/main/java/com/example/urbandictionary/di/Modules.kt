package com.example.urbandictionary.di

import com.example.urbandictionary.network.Webservices
import com.example.urbandictionary.network.repository.DictionaryRepository
import com.example.urbandictionary.network.repository.DictionaryRepositoryImpl
import com.example.urbandictionary.viewmodel.DictionaryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Webservices.instance }
    single<DictionaryRepository> { DictionaryRepositoryImpl(get()) }
    viewModel { DictionaryViewModel(get()) }
}