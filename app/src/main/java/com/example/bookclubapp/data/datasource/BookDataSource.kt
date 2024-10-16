package com.example.bookclubapp.data.datasource

import android.util.Log
import com.example.bookclubapp.data.entity.BookItem
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
    // Google Books API'den anasayfa kitapları
    suspend fun homepageKitapYukle(): List<VolumeInfo> =
        withContext(Dispatchers.IO) {
            return@withContext bdao.homepageKitapYukle().items?.map { it.volumeInfo } ?: emptyList()
        }

    // NYTimes API'den bestseller kitaplar
    suspend fun getBestsellerBooks(): List<VolumeInfo> =
        withContext(Dispatchers.IO) {
            val response = nyTimesApi.getBestsellerBooks(Constants.NYTIMES_API_KEY, "hardcover-fiction")
            if (response.results.books.isEmpty()) {
                Log.e("NYTimes API", "No books found in the response")
                return@withContext emptyList() // Boş liste döndür
            }

            // Bestseller kitapları listele
            val onlyBestsellers = response.results.books

            Log.d("NYTimes API Response", onlyBestsellers.toString())

            // Kitapları VolumeInfo'ya çevir ve döndür
            return@withContext onlyBestsellers.map { it.toVolumeInfo() }
        }


    fun NyTimesBook.toVolumeInfo(): VolumeInfo {
        return VolumeInfo(
            title = this.title ?: "Unknown Title",
            authors = if (!this.author.isNullOrEmpty()) listOf(this.author) else listOf("Unknown Author"),
            description = this.description ?: "Description not available",
            imageLinks = null  // NYTimes'te resim bilgisi olmadığı için null bırakıyoruz
        )
    }






    // Google Books API'den okunan kitaplar
    suspend fun getReadBooks(): List<VolumeInfo> =
        withContext(Dispatchers.IO) {
            return@withContext bdao.getReadBooks().items?.map { it.volumeInfo } ?: emptyList()
        }

    // Google Books API'den favori kitaplar
    suspend fun getFavoriteBooks(): List<VolumeInfo> =
        withContext(Dispatchers.IO) {
            return@withContext bdao.getFavoriteBooks().items?.map { it.volumeInfo } ?: emptyList()
        }

    // Google Books API ile kitap arama
    suspend fun searchBooks(query: String): List<VolumeInfo> =
        withContext(Dispatchers.IO) {
            return@withContext googleBooksApi.searchBooks(query, Constants.GOOGLE_BOOKS_BASE_URL).items?.map { it.volumeInfo } ?: emptyList()
        }
}

