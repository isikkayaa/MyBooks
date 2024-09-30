package com.example.bookclubapp.data.repository

import com.example.bookclubapp.data.datasource.BookDataSource
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.data.entity.VolumeInfo

class BooksRepository(var bds: BookDataSource)  {
    suspend fun homepageKitapYukle() : VolumeInfo = bds.homepageKitapYukle()


    suspend fun searchBooks(query: String, apiKey: String) = bds.searchBooks(query,apiKey)

    suspend fun favKitaplariYukle(): List<FavBooks> {
        val response = bds.favKitaplariYukle()
        return response.items?.map { favBookItem -> favBookItem.favBooks } ?: emptyList()
    }

}