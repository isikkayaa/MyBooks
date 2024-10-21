package com.example.bookclubapp.data.repository

import com.example.bookclubapp.data.datasource.BookDataSource
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.data.entity.VolumeInfo

class BooksRepository(private val bds: BookDataSource)  {

    suspend fun homepageKitapYukle(): List<VolumeInfo> = bds.homepageKitapYukle()


    suspend fun getBestsellerBooks(): List<VolumeInfo> = bds.getBestsellerBooks()


    suspend fun getReadBooks(): List<VolumeInfo> = bds.getReadBooks()

    suspend fun getFavoriteBooks(): List<VolumeInfo> = bds.getFavoriteBooks()

    suspend fun searchBooks(query: String): List<VolumeInfo> = bds.searchBooks(query)
}