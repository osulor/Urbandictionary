package com.example.urbandictionary.viewmodel.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.urbandictionary.network.MyApplication
import com.example.urbandictionary.network.repository.DictionaryRepository
import com.example.urbandictionary.viewmodel.DictionaryViewModel
import io.reactivex.disposables.CompositeDisposable

class DictionaryViewModelFactory(private val dictionaryRepository: DictionaryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DictionaryViewModel(dictionaryRepository, CompositeDisposable()) as T
    }
}