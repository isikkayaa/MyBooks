package com.example.bookclubapp.data.entity

import java.io.Serializable

data class FavBooks(val title: String,
                    val authors: List<String>?,
                    val description: String?,
                    val imageLinks: ImageLinks?,
) : Serializable
