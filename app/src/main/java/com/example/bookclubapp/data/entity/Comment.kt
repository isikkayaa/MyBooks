package com.example.bookclubapp.data.entity

data class Comment(
    val bookTitle: String,
    val userComment: String,
    val bookImageUrl: String? = null
)
