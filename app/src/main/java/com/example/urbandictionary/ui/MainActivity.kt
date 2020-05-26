package com.example.urbandictionary.ui

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.urbandictionary.R
import com.example.urbandictionary.model.Definition
import com.example.urbandictionary.network.Webservices
import com.example.urbandictionary.network.repository.DictionaryRepositoryImpl
import com.example.urbandictionary.viewmodel.DictionaryViewModel
import com.example.urbandictionary.viewmodel.viewModelFactory.DictionaryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: DictionaryViewModel
    private lateinit var dictionaryAdapter: DictionaryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dictionaryRepository = DictionaryRepositoryImpl(Webservices.instance)
        val viewModelFactory = DictionaryViewModelFactory(dictionaryRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DictionaryViewModel::class.java)

        checkConnectivity()
        setUpRecyclerView()
        observeData()

        searchButton.setOnClickListener {
            hideKeyboard(this)
            if (!searchView.text.isNullOrBlank()){
                val word = searchView.text.toString()
                retrieveData(word)
                searchView.text.clear()
            } else {
                Toast.makeText(this,"No word was entered, please enter a word",Toast.LENGTH_LONG).show()
            }
        }

        up_button.setOnClickListener { sortByMostThumbsUp(viewModel.definitions.value?.list) }
        down_button.setOnClickListener { sortByMostThumbsDown(viewModel.definitions.value?.list) }
    }

    fun observeData() {
        viewModel.definitions.observe(this, Observer { result ->
            dictionaryAdapter.definitionList.addAll(result.list)
            dictionaryAdapter.notifyDataSetChanged()
        })

        viewModel.errorMessage.observe(this, Observer {
            showErrorSnackbar(it)
        })

        viewModel.loadingState.observe(this, Observer {
            when (it) {
                DictionaryViewModel.LoadingState.LOADING -> displayProgressbar()
                DictionaryViewModel.LoadingState.SUCCESS -> displayList()
                DictionaryViewModel.LoadingState.ERROR -> Toast.makeText(
                    this,
                    "Error has occured",
                    Toast.LENGTH_LONG
                ).show()
                else -> Toast.makeText(this, "Another error has happened", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setUpRecyclerView() {
        wordsRV.layoutManager = LinearLayoutManager(this)
        dictionaryAdapter = DictionaryAdapter(mutableListOf())
        wordsRV.adapter = dictionaryAdapter
        val decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        wordsRV.addItemDecoration(decorator)
    }

    private fun displayProgressbar() {
        progressbar.visibility = View.VISIBLE
        wordsRV.visibility = View.GONE
    }

    private fun displayList() {
        wordsRV.visibility = View.VISIBLE
        progressbar.visibility = View.GONE
    }

    private fun retrieveData(word: String) {
        if (!hasInternetConnection()){
            showErrorSnackbar("No Internet, Please check your connexion ")
        } else {
            dictionaryAdapter.definitionList.clear()
            viewModel.getDefinitionFromApi(word)

        }
    }

    private fun sortByMostThumbsUp(definitionList: List<Definition>?) {
        val sortedList = definitionList?.sortedByDescending { definition ->
            definition.thumbs_up
        }
        updateWordList(sortedList)
    }

    private fun sortByMostThumbsDown(definitionList: List<Definition>?) {
        val sortedList = definitionList?.sortedByDescending { definition ->
            definition.thumbs_down
        }
        updateWordList(sortedList)
    }

    private fun updateWordList(newDefinitionList: List<Definition>?) {
        dictionaryAdapter.definitionList.clear()
        dictionaryAdapter.definitionList.addAll(newDefinitionList!!)
        dictionaryAdapter.notifyDataSetChanged()
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showErrorSnackbar(errorMessage: String){
        val snackBar = Snackbar.make(
            wordsRV,
            errorMessage,
            Snackbar.LENGTH_LONG
        )
        snackBar.setActionTextColor(resources.getColor(R.color.snackBarTextColor))
        snackBar.view.background = resources.getDrawable(R.color.failSnackBarColor)
        snackBar.duration = 3000
        snackBar.show()
    }

    private fun checkConnectivity(){
        if (!hasInternetConnection()){
            showErrorSnackbar("No Internet, Please check your connexion ")
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = application.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}
