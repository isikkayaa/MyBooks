package com.example.bookclubapp.data.datasource

import android.util.Log
import com.example.bookclubapp.data.entity.BookItem
import com.example.bookclubapp.data.entity.BooksResponse
import com.example.bookclubapp.data.entity.ImageLinks
import com.example.bookclubapp.data.entity.NyTimesBook
import com.example.bookclubapp.data.entity.NyTimesBookResult
import com.example.bookclubapp.data.entity.VolumeInfo
import com.example.bookclubapp.retrofit.ApiUtils
import com.example.bookclubapp.retrofit.BooksDao
import com.example.bookclubapp.retrofit.GoogleBooksApi
import com.example.bookclubapp.retrofit.NyTimesApi
import com.example.bookclubapp.util.Constants
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class BookDataSource(private val bdao: BooksDao,
                     private val googleBooksApi: GoogleBooksApi,
                     private val nyTimesApi: NyTimesApi
) {

    suspend fun homepageKitapYukle(): List<VolumeInfo> =
        withContext(Dispatchers.IO) {
            return@withContext bdao.homepageKitapYukle().items?.map { it.volumeInfo } ?: emptyList()
        }


    suspend fun getBestsellerBooks(): List<VolumeInfo> =
        withContext(Dispatchers.IO) {
            val response = nyTimesApi.getBestsellerBooks(Constants.NYTIMES_API_KEY, "hardcover-fiction")
            if (response.results.books.isEmpty()) {
                Log.e("NYTimes API", "No books found in the response")
                return@withContext emptyList() // Boş liste döndür
            }


            val onlyBestsellers = response.results.books

            Log.d("NYTimes API Response", onlyBestsellers.toString())


            return@withContext onlyBestsellers.map { it.toVolumeInfo() }
        }


    fun NyTimesBook.toVolumeInfo(): VolumeInfo {
        return VolumeInfo(
            title = this.title ?: "Unknown Title",
            authors = if (!this.author.isNullOrEmpty()) listOf(this.author) else listOf("Unknown Author"),
            description = this.description ?: "Description not available",
            imageLinks = null
        )
    }





/*

    suspend fun getReadBooks(): List<VolumeInfo> =
        withContext(Dispatchers.IO) {
            return@withContext bdao.getReadBooks().items?.map { it.volumeInfo } ?: emptyList()
        }


    suspend fun getFavoriteBooks(): List<VolumeInfo> =
        withContext(Dispatchers.IO) {
            return@withContext bdao.getFavoriteBooks().items?.map { it.volumeInfo } ?: emptyList()
        }

 */


    suspend fun searchBooks(query: String): List<VolumeInfo> =
        withContext(Dispatchers.IO) {
            return@withContext googleBooksApi.searchBooks(query, Constants.GOOGLEBOOKS_API_KEY).items?.map { it.volumeInfo } ?: emptyList()
        }
}


