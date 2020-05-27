package com.example.urbandictionary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.urbandictionary.model.UrbanResponse
import com.example.urbandictionary.network.repository.DictionaryRepository
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException

class DictionaryViewModel(private val dictionaryRepository: DictionaryRepository) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    private var definitionsMutableLiveData: MutableLiveData<UrbanResponse> = MutableLiveData()
    val definitions: LiveData<UrbanResponse>
        get() = definitionsMutableLiveData

    private var errorMessageMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = errorMessageMutableLiveData

    private var loadingStateMutableLiveData = MutableLiveData<LoadingState>()
    val loadingState : LiveData<LoadingState>
        get() = loadingStateMutableLiveData

    enum class LoadingState {
        LOADING,
        SUCCESS,
        ERROR
    }

    fun getDefinitionFromApi(word: String) {
        loadingStateMutableLiveData.value = LoadingState.LOADING
        disposable.add(
            dictionaryRepository.getDefinition(word).subscribe({ result ->
                if (result.list.isEmpty()) {
                    errorMessageMutableLiveData.value = "Word Not Found"
                    loadingStateMutableLiveData.value = LoadingState.ERROR
                } else {
                    definitionsMutableLiveData.value = result
                    loadingStateMutableLiveData.value = LoadingState.SUCCESS
                }
            }, {
                when (it) {
                    is UnknownHostException -> errorMessageMutableLiveData.value = "Network Error Occurred"
                    else -> errorMessageMutableLiveData.value = it.localizedMessage
                }
                loadingStateMutableLiveData.value = LoadingState.ERROR
            })
        )
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}