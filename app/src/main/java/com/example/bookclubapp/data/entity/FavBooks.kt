package com.example.bookclubapp.data.entity

import java.io.Serializable

data class FavBooks(val authors: List<String>?,
                    val description: String?,
                    val imageLinks: ImageLinks?,
                    val title: String
) : Serializable
