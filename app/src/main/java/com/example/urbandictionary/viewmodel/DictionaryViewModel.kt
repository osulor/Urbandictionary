package com.example.urbandictionary.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.urbandictionary.model.UrbanResponse
import com.example.urbandictionary.network.MyApplication
import com.example.urbandictionary.network.repository.DictionaryRepository
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException

class DictionaryViewModel(
    private val dictionaryRepository: DictionaryRepository,
    private val disposable: CompositeDisposable
) : ViewModel(){

     var definitions: MutableLiveData<UrbanResponse> = MutableLiveData()
     val errorMessage: MutableLiveData<String> = MutableLiveData()
     val loadingState = MutableLiveData<LoadingState>()

    enum class LoadingState {
        LOADING,
        SUCCESS,
        ERROR
    }

     fun getDefinitionFromApi(word: String){
        loadingState.value = LoadingState.LOADING
        disposable.add(
            dictionaryRepository.getDefinition(word).subscribe({result ->

                if (result.list.isEmpty()){
                    errorMessage.value = "Word Not Found"
                    loadingState.value = LoadingState.ERROR
                } else {
                    definitions.value = result
                    loadingState.value = LoadingState.SUCCESS
                }

            },{
                when (it) {
                    is UnknownHostException -> errorMessage.value = "Network Error Occurred"
                    else -> errorMessage.value = it.localizedMessage
                }

                loadingState.value = LoadingState.ERROR
            })
        )
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}