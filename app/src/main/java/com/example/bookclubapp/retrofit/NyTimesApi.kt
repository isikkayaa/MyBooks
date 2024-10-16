package com.example.bookclubapp.retrofit

import com.example.bookclubapp.data.entity.BooksResponse
import com.example.bookclubapp.data.entity.NyTimesBooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NyTimesApi {


    @GET("hardcover-fiction.json")
    suspend fun getBestsellerBooks(
        @Query("api-key") apiKey: String,
        @Query("list") listName: String = "hardcover-fiction"
    ): NyTimesBooksResponse

}