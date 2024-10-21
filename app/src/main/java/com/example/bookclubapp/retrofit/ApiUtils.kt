package com.example.bookclubapp.retrofit

import com.example.bookclubapp.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ApiUtils {
    companion object {
        val BASE_URL = "https://api.nytimes.com/svc/books/v3/lists/current/"


        fun getGoogleBooksApi(): GoogleBooksApi {
            return RetrofitClient.getClient(Constants.GOOGLE_BOOKS_BASE_URL).create(GoogleBooksApi::class.java)
        }

        fun getBooksDao() : BooksDao {
            return RetrofitClient.getClient(Constants.GOOGLE_BOOKS_BASE_URL).create(BooksDao::class.java)
        }

        fun getNyTimesApi() : NyTimesApi {
            return RetrofitClient.getClient(BASE_URL).create(NyTimesApi::class.java)
        }





    }


}