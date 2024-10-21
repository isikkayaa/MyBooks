package com.example.bookclubapp.retrofit

import com.example.bookclubapp.data.entity.BookItem
import com.example.bookclubapp.data.entity.BooksResponse
import com.example.bookclubapp.data.entity.NyTimesBooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksDao {
    @GET("books/v1/volumes")
    suspend fun homepageKitapYukle(): BooksResponse

    @GET("lists.json")
    suspend fun getBestsellerBooks(
        @Query("api-key") apiKey: String,
        @Query("list") listName: String = "hardcover-fiction"
    ): BooksResponse
    @GET("books/v1/volumes?q=read")
    suspend fun getReadBooks(): BooksResponse

    @GET("books/v1/volumes?q=favorites")
    suspend fun getFavoriteBooks(): BooksResponse
    @GET("books/v1")
    suspend fun searchBooks(query:String, apiKey:String) : BookItem
}

