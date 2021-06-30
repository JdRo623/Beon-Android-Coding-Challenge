package com.example.techchallenge.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.techchallenge.model.WordsService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class WordsListViewModel: ViewModel() {

    private val wordsService = WordsService()
    private val disposable = CompositeDisposable()
    private var refreshDataDisposable = CompositeDisposable()

    val words = MutableLiveData<HashMap<String,Int>>()
    val wordsFetched = MutableLiveData<HashMap<String,Int>>()

    val loading = MutableLiveData<Boolean>(false)
    val progressPercentage= MutableLiveData<Int>()
    private val maxFileSize:Long = 30000000

    fun refreshWords(){
        fetchWords()
    }

    private fun fetchWords(){
        loading.value = true
        wordsFetched.value = hashMapOf()
        words.value = hashMapOf()
        progressPercentage.postValue(0)

        disposable.add(
            wordsService.getWords()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Call<ResponseBody>>(){
                    override fun onError(e: Throwable?) {
                        loading.value = false
                    }
                    override fun onNext(value: Call<ResponseBody>?) {
                        value?.enqueue(object : Callback<ResponseBody>{
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.isSuccessful) {
                               processFile(response.body())
                                }
                            }
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                loading.value = false
                            }
                        })
                    }
                    override fun onComplete() {
                        loading.value = false
                        progressPercentage.postValue(100)
                    }
                })
        )
    }

    private fun processRawData(body: ResponseBody?): Boolean {
        return try {

            var inputStream: InputStream? = null
            val isr: InputStreamReader?
            val reader: BufferedReader?
            var line: String?

            try {
                val fileReader = ByteArray(4096)
                var fileSizeProcessed: Long = 0
                inputStream = body?.byteStream()
                isr = InputStreamReader(inputStream, "ISO-8859-1")
                reader = BufferedReader(isr)

                while (reader.readLine().also { line = it } != null){
                    val read: Int = inputStream!!.read(fileReader)
                    fileSizeProcessed += read.toLong()
                    calculateProcessPercentage(fileSizeProcessed)
                    val re = "[^A-Za-z0-9 ]".toRegex()
                    val row: List<String>? = re?.replace(line!!, " ")?.split(",")
                    processWords(row!!)
                }
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }

    private fun calculateProcessPercentage(current: Long){
        val percentage: Float = (current.toFloat()/maxFileSize.toFloat())
        progressPercentage.postValue((percentage*100).toInt())
    }

    private fun processWords(row: List<String>){
        row.forEach {item: String ->
            val rawWords: List<String>? = item.split(" ")
            rawWords?.forEach{word: String ->
                if(word.length>=5){
                    val currentcount: Int? = wordsFetched.value?.get(word)
                    if(currentcount==null){
                        wordsFetched.value?.put(word.lowercase(),1)
                    }else{
                        wordsFetched.value?.put(word.lowercase(), currentcount+1)
                    }
                }
            }
        }
        wordsFetched.value?.let {
            words.postValue(HashMap(it))
        }
    }

    private fun processFile(body: ResponseBody?)  {
       Observable.fromCallable {
           val processedFile = processRawData(body)

           true
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
           .doOnComplete {
               words.value = HashMap(wordsFetched.value)
           }
            .subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        refreshDataDisposable.clear()
    }

}