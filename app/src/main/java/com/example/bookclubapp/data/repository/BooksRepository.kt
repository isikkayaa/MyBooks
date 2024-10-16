package com.example.bookclubapp.data.repository

import com.example.bookclubapp.data.datasource.BookDataSource
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.data.entity.VolumeInfo

class BooksRepository(private val bds: BookDataSource)  {

    suspend fun homepageKitapYukle(): List<VolumeInfo> = bds.homepageKitapYukle()

    // Bestseller kitapları NYTimes API ile çek
    suspend fun getBestsellerBooks(): List<VolumeInfo> = bds.getBestsellerBooks()

    // Okunan kitaplar için Google Books API'yi kullan
    suspend fun getReadBooks(): List<VolumeInfo> = bds.getReadBooks()

    // Favori kitaplar için Google Books API'yi kullan
    suspend fun getFavoriteBooks(): List<VolumeInfo> = bds.getFavoriteBooks()

    // Kitap aramaları için Google Books API'yi kullan
    suspend fun searchBooks(query: String): List<VolumeInfo> = bds.searchBooks(query)
}