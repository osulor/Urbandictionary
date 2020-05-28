package com.example.urbandictionary.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.urbandictionary.R
import com.example.urbandictionary.model.Definition
import com.example.urbandictionary.utils.setDate
import kotlinx.android.synthetic.main.word_item.view.*

class DictionaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bindItem(definition: Definition){
        itemView.apply {
            word_text.text = definition.word
            word_meaning.text = definition.definition
            authorTv.text = definition.author
            thumbs_down_count.text = definition.thumbs_down.toString()
            thumbs_up_count.text = definition.thumbs_up.toString()
            exampleTv.text = context.getString(R.string.example).plus(definition.example)
            dateTv.text = context.getString(R.string.written_on).plus(setDate(definition.written_on))
        }
    }
}