package com.example.bookclubapp.data.entity

import java.io.Serializable

data class FavBooks(
    val title: String = "",
    val authors: List<String>? = null,
    val description: String? = null,
    val imageLinks: ImageLinks? = null
)

