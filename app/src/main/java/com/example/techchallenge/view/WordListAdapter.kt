package com.example.techchallenge.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techchallenge.R
import kotlinx.android.synthetic.main.item_word.view.*

class WordListAdapter(var words: MutableList<Map.Entry<String, Int>> ): RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    fun updateWords(newWords: MutableList<Map.Entry<String, Int>> ){
        words.clear()
        words.addAll(newWords)
        notifyDataSetChanged()
    }

    class  WordViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val wordName = view.word_name
        private val count = view.count

        fun bind(entry: Map.Entry<String,Int>){
            wordName.text = entry.key.toString()
            count.text = entry.value.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WordViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
    )

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(words.asIterable().elementAt(position))
    }

    override fun getItemCount() = words.size
}