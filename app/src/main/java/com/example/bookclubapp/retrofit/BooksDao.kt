package com.example.bookclubapp.retrofit

import com.example.bookclubapp.data.entity.BookItem
import com.example.bookclubapp.data.entity.BooksResponse
import com.example.bookclubapp.data.entity.FavBookItem
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.data.entity.FavBooksResponse
import com.example.bookclubapp.data.entity.VolumeInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksDao {
    @GET("volumes") // "books/v1" yerine "volumes" endpoint'ini kontrol edin.
    suspend fun homepageKitapYukle(): BookItem


    @GET("your-fav-books-endpoint")
    suspend fun favKitaplariYukle(): FavBooksResponse // Bu, FavBooksResponse dönecek

    @GET("books/v1")
    suspend fun searchBooks(query:String, apiKey:String) : BookItem
}

