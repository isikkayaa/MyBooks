package com.example.bookclubapp.retrofit

import com.example.bookclubapp.data.entity.ApiResponse
import com.example.bookclubapp.data.entity.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {

    @GET("books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): BooksResponse
}


