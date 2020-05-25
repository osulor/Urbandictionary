package com.example.urbandictionary.ui

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.urbandictionary.model.Definition
import kotlinx.android.synthetic.main.word_item.view.*
import java.text.SimpleDateFormat

class DictionaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindItem(definition: Definition){

        itemView.apply {
            word_text.text = definition.word
            word_meaning.text = definition.definition
            authorTv.text = definition.author
            thumbs_down_count.text = definition.thumbs_down.toString()
            thumbs_up_count.text = definition.thumbs_up.toString()
            exampleTv.text = "Example: ".plus(definition.example)
            dateTv.text = "Written on ".plus(setDate(definition.written_on))
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun setDate(writtenOn: String?): String{
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("MM/dd/yyyy")
       return formatter.format(parser.parse(writtenOn))
    }

}