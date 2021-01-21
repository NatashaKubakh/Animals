package com.example.animals.model

import com.example.animals.di.DaggerApiComponent
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AnimalApiService {

    private val BASE_URL = "https://us-central1-apis-4674e.cloudfunctions.net"

    @Inject
    lateinit var  api: AnimalApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getApiKey(): Single<ApiKey> {
        return api.getApiKey()
    }

    fun getAnimals(key: String): Single<List<Animal>> {
        return api.getAnimals(key)
    }
}