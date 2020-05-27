package com.example.urbandictionary.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.urbandictionary.R
import com.example.urbandictionary.model.Definition

class DictionaryAdapter(val definitionList: MutableList<Definition>) :
    RecyclerView.Adapter<DictionaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        return DictionaryViewHolder(view)
    }

    override fun getItemCount() = definitionList.size

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        val currentDefinition = definitionList[position]
        holder.bindItem(currentDefinition)
    }

    fun updateDefinitionsList(newDefinitionList: List<Definition>?) {
        definitionList.clear()
        definitionList.addAll(newDefinitionList!!)
        notifyDataSetChanged()
    }

    fun sortByMostThumbsUp(definitionList: List<Definition>?) {
        val sortedList = definitionList?.sortedByDescending { definition ->
            definition.thumbs_up
        }
        updateDefinitionsList(sortedList)
    }

    fun sortByMostThumbsDown(definitionList: List<Definition>?) {
        val sortedList = definitionList?.sortedByDescending { definition ->
            definition.thumbs_down
        }
        updateDefinitionsList(sortedList)
    }
}