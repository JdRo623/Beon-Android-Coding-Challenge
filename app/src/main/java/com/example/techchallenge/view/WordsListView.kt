package com.example.techchallenge.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.techchallenge.R
import com.example.techchallenge.util.WordsUtil
import com.example.techchallenge.viewmodel.WordsListViewModel
import kotlinx.android.synthetic.main.fragment_word_list.*

class WordsListView:Fragment() {

    val wordsViewModel: WordsListViewModel  by activityViewModels()
    private var wordsAdapter = WordListAdapter(mutableListOf())
    val wordsUtil = WordsUtil()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       // wordsViewModel.refreshWords()

        return inflater.inflate(R.layout.fragment_word_list, container, false)
    }

    override fun onStart() {
        wordsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = wordsAdapter
        }

        observeViewModel()
        super.onStart()
    }



    fun observeViewModel() {
        wordsViewModel.words.observe(this.viewLifecycleOwner, Observer {words  ->
            words?.let {wordsAdapter.updateWords((wordsUtil.sortWordsHashMap(it).toMutableList())) }
        })
    }

}