package com.example.bookclubapp.data.datasource

import com.example.bookclubapp.data.entity.BookItem
import com.example.bookclubapp.data.entity.FavBookItem
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.data.entity.FavBooksResponse
import com.example.bookclubapp.data.entity.VolumeInfo
import com.example.bookclubapp.retrofit.BooksDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookDataSource(var bdao: BooksDao) {
    suspend fun homepageKitapYukle(): VolumeInfo =
        withContext(Dispatchers.IO) {
            return@withContext bdao.homepageKitapYukle().volumeInfo
        }

    suspend fun favKitaplariYukle(): FavBooksResponse =
        withContext(Dispatchers.IO) {
            return@withContext bdao.favKitaplariYukle() // Bu, response'dan 'FavBooksResponse' dönecek
        }


    suspend fun searchBooks(query: String, apiKey: String): BookItem =
        withContext(Dispatchers.IO) {
            return@withContext bdao.searchBooks( query,apiKey)
        }
}

