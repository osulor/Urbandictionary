package com.example.urbandictionary.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun setDate(writtenOn: String?): String{
    val parser = SimpleDateFormat("yyyy-MM-dd")
    val formatter = SimpleDateFormat("MM/dd/yyyy")
    return formatter.format(parser.parse(writtenOn))
}