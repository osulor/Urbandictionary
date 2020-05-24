package com.example.urbandictionary.model

data class Definition(
    var definition: String,
    var example: String,
    var thumbs_up: Int,
    var thumbs_down: Int,
    var author: String
)
