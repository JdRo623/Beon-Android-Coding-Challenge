package com.example.techchallenge.util

import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class WordsUtil{

    fun sortWordsHashMap(words: HashMap<String,Int>): List<Map.Entry<String, Int>> {
        val sortedEntries: List<Map.Entry<String, Int>> = ArrayList(words.toMap().entries)

        Collections.sort(sortedEntries
        ) { p0, p1 -> p1?.value!!.compareTo(p0?.value!!) }

        return sortedEntries
    }

}