package com.example.bookclubapp.data.entity

import com.google.gson.annotations.SerializedName

data class NyTimesBookResult(
    @SerializedName("books") val books: List<NyTimesBook>  // Eğer "books" listesi var ise

)
