package com.example.urbandictionary.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.urbandictionary.R
import com.example.urbandictionary.model.Definition

class DictionaryAdapter(private val definitionList: List<Definition>): RecyclerView.Adapter<DictionaryAdapter.DictionaryViewHolder>(){

    inner class  DictionaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_item,parent,false)
        return DictionaryViewHolder(view)
    }

    override fun getItemCount() = definitionList.size

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}