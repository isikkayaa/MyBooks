package com.example.bookclubapp.data.entity

data class CurrentlyBookList(val bookTitle: String,
                             val bookAuthors : List<String>?,
                             val bookImageUrl: String? = null)
