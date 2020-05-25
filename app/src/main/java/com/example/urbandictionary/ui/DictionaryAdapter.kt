package com.example.urbandictionary.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.urbandictionary.R
import com.example.urbandictionary.model.Definition
import kotlinx.android.synthetic.main.activity_main.view.*

class DictionaryAdapter(val definitionList: MutableList<Definition>): RecyclerView.Adapter<DictionaryViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_item,parent,false)
        return DictionaryViewHolder(view)
    }

    override fun getItemCount() = definitionList.size

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        holder.bindItem(definitionList[position])

    }


}