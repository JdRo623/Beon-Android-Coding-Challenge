package com.example.techchallenge.model

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit

class WordsService {
    private val BASE_URL = "https://dl.dropbox.com/"
    private val api: WordsApi

    init {
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient().newBuilder().build())
            .build()
            .create(WordsApi::class.java)
    }

    fun getWords(): Observable <Call<ResponseBody>> {
        return Observable.create(ObservableOnSubscribe<Call<ResponseBody>> { emitter ->
            emitter.onNext(api.getWords())
            emitter.onComplete()
        })
    }
}