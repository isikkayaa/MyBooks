package com.example.bookclubapp.data.entity

import com.google.gson.annotations.SerializedName

data class NyTimesBook(@SerializedName("title") val title: String?,
                       @SerializedName("author") val author: String?,
                       @SerializedName("description") val description: String?)
