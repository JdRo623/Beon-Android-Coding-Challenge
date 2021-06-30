package com.example.techchallenge.model

import retrofit2.Call;
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url




interface WordsApi {

    @GET("s/frkejggpxyklc68/test.csv?dl=1")
    @Streaming
    fun getWords(): Call<ResponseBody>

}