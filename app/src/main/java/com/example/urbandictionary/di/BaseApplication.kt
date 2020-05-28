package com.example.urbandictionary.di

import android.app.Application
import com.example.urbandictionary.R
import com.example.urbandictionary.di.BaseApplication.DatasourceProperties.API_KEY_PROPERTY
import com.example.urbandictionary.di.BaseApplication.DatasourceProperties.BASE_URL_PROPERTY
import com.example.urbandictionary.di.BaseApplication.DatasourceProperties.HOST_PROPERTY
import org.koin.android.ext.android.setProperty
import org.koin.android.ext.android.startKoin

class BaseApplication: Application() {

    private lateinit var BASE_URL: String
    private lateinit var API_KEY: String
    private lateinit var HOST: String

    override fun onCreate() {
        super.onCreate()

        startKoin (this, listOf(viewModelModule, repositoryModule, networkModule))

        BASE_URL = getString(R.string.BASE_URL)
        API_KEY = getString(R.string.API_KEY)
        HOST = getString(R.string.HOST)
        setProperty(BASE_URL_PROPERTY, BASE_URL)
        setProperty(API_KEY_PROPERTY, API_KEY)
        setProperty(HOST_PROPERTY, HOST)

    }

    object DatasourceProperties {
        const val  BASE_URL_PROPERTY = "BASE_URL_PROPERTY"
        const val  API_KEY_PROPERTY = "API_KEY_PROPERTY"
        const val  HOST_PROPERTY = "HOST_PROPERTY"
    }
}