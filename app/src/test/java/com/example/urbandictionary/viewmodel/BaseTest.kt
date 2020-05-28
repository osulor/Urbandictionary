package com.example.urbandictionary.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.urbandictionary.di.BaseApplication
import com.example.urbandictionary.di.networkModule
import com.example.urbandictionary.di.repositoryModule
import com.example.urbandictionary.di.viewModelModule
import io.mockk.MockKAnnotations
import org.junit.After
import org.junit.Rule
import org.koin.standalone.StandAloneContext
import org.koin.standalone.setProperty
import org.koin.test.KoinTest

abstract class BaseTest: KoinTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    protected lateinit var BASE_URL: String
    protected lateinit var API_KEY: String
    protected lateinit var HOST: String

    open fun before() {
        MockKAnnotations.init(this)
        StandAloneContext.startKoin(listOf(viewModelModule, repositoryModule, networkModule))

        BASE_URL = "https://mock.com"
        API_KEY = "api_key"
        HOST = "host"
        setProperty(BaseApplication.DatasourceProperties.BASE_URL_PROPERTY, BASE_URL)
        setProperty(BaseApplication.DatasourceProperties.API_KEY_PROPERTY, API_KEY)
        setProperty(BaseApplication.DatasourceProperties.HOST_PROPERTY, HOST)
    }

    @After
    fun tearDown() {
        StandAloneContext.stopKoin()
    }
}