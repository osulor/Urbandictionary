package com.example.urbandictionary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.urbandictionary.R
import com.example.urbandictionary.network.Webservices
import com.example.urbandictionary.network.repository.DictionaryRepository
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
        viewModel = ViewModelProvider(this,viewModelFactory).get(DictionaryViewModel::class.java)

        setUpRecyclerView()
        observeData()

        searchButton.setOnClickListener {
            val word = searchView.text.toString()
            retrieveData(word)
        }

    }

    fun observeData(){
        viewModel.definitions.observe(this, Observer {result ->
            dictionaryAdapter.definitionList.addAll(result.list)
            dictionaryAdapter.notifyDataSetChanged()
        })

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        })

        viewModel.loadingState.observe(this, Observer {
            when(it) {
                DictionaryViewModel.LoadingState.LOADING -> displayProgressbar()
                DictionaryViewModel.LoadingState.SUCCESS -> displayList()
                DictionaryViewModel.LoadingState.ERROR -> Toast.makeText(this,"Error has happened",Toast.LENGTH_LONG).show()
                else -> Toast.makeText(this,"Another error has happened",Toast.LENGTH_LONG).show()
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

    fun retrieveData(word: String){
        viewModel.getDefinitionFromApi(word)
        showSuccessSnackBar()
    }

    fun showSuccessSnackBar() {
        Snackbar.make(
            wordsRV,
            "Data have successfully been retrieved. ",
            Snackbar.LENGTH_LONG
        ).show()
    }
}
