package com.example.bookclubapp.data.entity

import com.google.gson.annotations.SerializedName

data class NyTimesBooksResponse(
    @SerializedName("results") val results: NyTimesBookResult
)
