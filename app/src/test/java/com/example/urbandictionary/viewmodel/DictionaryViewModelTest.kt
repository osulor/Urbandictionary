package com.example.urbandictionary.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.urbandictionary.model.Definition
import com.example.urbandictionary.model.UrbanResponse
import com.example.urbandictionary.network.repository.DictionaryRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import java.net.UnknownHostException

@RunWith(BlockJUnit4ClassRunner::class)
class DictionaryViewModelTest {

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var viewModel: DictionaryViewModel
    @MockK
    lateinit var dictionaryRepository: DictionaryRepository
    @MockK
    lateinit var compositeDisposable: CompositeDisposable

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = DictionaryViewModel(dictionaryRepository,compositeDisposable)
        every { compositeDisposable.add(any()) } returns true
    }

    @Test
    fun `getDefinitions When Repository Returns a Non EmptyList`(){

        val definitonList = listOf(
            Definition("dummyWord","dummyDefinition","dummyExample",45,22,"dummyAuthor","dummyDate"),
            Definition("dummyWord1","dummyDefinition1","dummyExample1",45,22,"dummyAuthor1","dummyDate1"),
            Definition("dummyWord2","dummyDefinition2","dummyExample",444,45,"dummyAuthor2","dummyDate2"),
            Definition("dummyWord3","dummyDefinition3","dummyExample3",45,22,"dummyAuthor3","dummyDate3")
        )

        val urbanResponse = UrbanResponse(definitonList)
        every { dictionaryRepository.getDefinition("dummyWord") } returns Single.just(urbanResponse)
        viewModel.getDefinitionFromApi("dummyWord")

        assertEquals(urbanResponse,viewModel.definitions.value)
        assertEquals(null,viewModel.errorMessage.value)
        assertEquals(DictionaryViewModel.LoadingState.SUCCESS,viewModel.loadingState.value)
    }

    @Test
    fun `Show Error When Repository Returns EmptyList`(){

        every { dictionaryRepository.getDefinition("dummyWord") } returns Single.just(
            UrbanResponse(
                emptyList())
        )

        viewModel.getDefinitionFromApi("dummyWord")
        assertEquals(null,viewModel.definitions.value)
        assertEquals("Word Not Found",viewModel.errorMessage.value)
        assertEquals(DictionaryViewModel.LoadingState.ERROR,viewModel.loadingState.value)

    }

    @Test
    fun getPicsShowsNetworkError_WhenRepositoryReturnsUnknownHostException(){
        every { dictionaryRepository.getDefinition("dummyWord") } returns Single.error(
            UnknownHostException()
        )
        viewModel.getDefinitionFromApi("dummyWord")
        assertEquals(null,viewModel.definitions.value)
        assertEquals("Network Error Occurred",viewModel.errorMessage.value)
        assertEquals(DictionaryViewModel.LoadingState.ERROR,viewModel.loadingState.value)
    }

    @Test
    fun getPicsShowLocalizedError_WhenRepositoryReturnsOthersTypeOfException(){
        every { dictionaryRepository.getDefinition("dummyWord") } returns Single.error(RuntimeException("This is a custom exception"))
        viewModel.getDefinitionFromApi("dummyWord")
        assertEquals(null,viewModel.definitions.value)
        assertEquals("This is a custom exception",viewModel.errorMessage.value)
        assertEquals(DictionaryViewModel.LoadingState.ERROR,viewModel.loadingState.value)
    }

    @After
    fun tearDown() {

    }
}