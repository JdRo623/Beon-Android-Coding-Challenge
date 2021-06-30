package com.example.techchallenge.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.techchallenge.R
import com.example.techchallenge.viewmodel.WordsListViewModel
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_control.*

class WordsControlsView:Fragment() {

    val wordsViewModel: WordsListViewModel  by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_control, container, false)
    }

    override fun onStart() {
        super.onStart()
        btn_start.setOnClickListener {
            if(!wordsViewModel.loading.value!!)
                wordsViewModel.refreshWords()
        }
        progressBar.max = 100
        observeViewModel()
        //progressBar.progress =
    }
    private fun observeViewModel() {
        wordsViewModel.progressPercentage.observe(this.viewLifecycleOwner, Observer {progressPercentage  ->
            progressPercentage?.let {progressBar.progress =it}
        })
    }
}