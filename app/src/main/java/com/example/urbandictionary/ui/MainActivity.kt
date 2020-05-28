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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.urbandictionary.R
import com.example.urbandictionary.ui.adapter.DictionaryAdapter
import com.example.urbandictionary.viewmodel.DictionaryViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var dictionaryAdapter: DictionaryAdapter
    private val viewModel: DictionaryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkConnectivity()
        setUpRecyclerView()
        observeData()

        searchButton.setOnClickListener {
            hideKeyboard(this)
            if (!searchView.text.isNullOrBlank()) {
                val word = searchView.text.toString()
                retrieveData(word)
                searchView.text.clear()
            } else {
                Toast.makeText(this, getString(R.string.no_word_entered), Toast.LENGTH_LONG)
                    .show()
            }
        }

        up_button.setOnClickListener { dictionaryAdapter.sortByMostThumbsUp(viewModel.definitions.value?.list) }
        down_button.setOnClickListener { dictionaryAdapter.sortByMostThumbsDown(viewModel.definitions.value?.list) }
    }

     fun observeData() {
        viewModel.definitions.observe(this, Observer { result ->
            dictionaryAdapter.updateDefinitionsList(result.list)
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
                    getString(R.string.error_ocurred),
                    Toast.LENGTH_LONG
                ).show()
                else -> Toast.makeText(this, getString(R.string.another_error), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setUpRecyclerView() {
        wordsRV.layoutManager = LinearLayoutManager(this)
        dictionaryAdapter =
            DictionaryAdapter(
                mutableListOf()
            )
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
        if (!hasInternetConnection()) {
            showErrorSnackbar(getString(R.string.no_internet))
        } else {
            dictionaryAdapter.definitionList.clear()
            viewModel.getDefinitionFromApi(word)
        }
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

    private fun showErrorSnackbar(errorMessage: String) {
        val snackBar = Snackbar.make(
            wordsRV,
            errorMessage,
            Snackbar.LENGTH_LONG
        )
        snackBar.setActionTextColor(resources.getColor(R.color.colorWhite))
        snackBar.view.background = resources.getDrawable(R.color.failSnackBarColor)
        snackBar.duration = 3000
        snackBar.show()
    }

    private fun checkConnectivity() {
        if (!hasInternetConnection()) {
            showErrorSnackbar(getString(R.string.no_internet))
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
