package com.example.animals

import android.app.Application
import org.junit.Rule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.animals.di.AppModule
import com.example.animals.di.DaggerViewModelComponent
import com.example.animals.model.Animal
import com.example.animals.model.AnimalApiService
import com.example.animals.model.ApiKey
import com.example.animals.util.SharedPreferencesHelper
import com.example.animals.viewModel.ListViewModel
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.schedulers.ExecutorScheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class ListViewModelTest {
    @get: Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var animalService: AnimalApiService

    @Mock
    lateinit var prefs: SharedPreferencesHelper

    var application = Mockito.mock(Application::class.java)

    var listViewModel = ListViewModel(application, true)

    private val key = "Test key"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(animalService))
            .prefsModule(PrefsModuleTest(prefs))
            .build()
            .inject(listViewModel)
    }

    @Test
    fun getAnimalsSuccess() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(key)
        val animal = Animal("cow", null, null, null, null, null, null)
        val animalList = listOf(animal)
        val testSingle = Single.just(animalList)
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(1, listViewModel.animals.value?.size)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getAnimalFailure() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(key)
        val testSingle = Single.error<List<Animal>>(Throwable())
        val keySingle = Single.just(ApiKey("OK", key))
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)
        listViewModel.refresh()
        Assert.assertEquals(null, listViewModel.animals.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true, listViewModel.loadError.value)

    }

    @Test
    fun getKeySuccess() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(null)
        val apiKey = ApiKey("OK", key)
        val keySingle = Single.just(apiKey)
        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)
        val animal = Animal("cow", null, null, null, null, null, null)
        val animalList = listOf(animal)
        val testSingle = Single.just(animalList)
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(1, listViewModel.animals.value?.size)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getKeyFailure() {
        Mockito.`when`(prefs.getApiKey()).thenReturn(null)
        val keySingle = Single.error<ApiKey>(Throwable())
        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)
        listViewModel.refresh()
        Assert.assertEquals(null, listViewModel.animals.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true, listViewModel.loadError.value)
    }


    @Before
    fun setupRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true, true)
            }
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }
}