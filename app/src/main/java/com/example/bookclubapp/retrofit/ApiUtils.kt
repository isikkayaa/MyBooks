package com.example.bookclubapp.retrofit

class ApiUtils {
    companion object {
        val BASE_URL = "https://www.googleapis.com/"



        /*fun getBooksDao() : BooksDao {
            return RetrofitClient.getClient(BASE_URL).create(BooksDao::class.java)
        }

        */

        fun getGoogleBooksApi(): GoogleBooksApi {
            return RetrofitClient.getClient(BASE_URL).create(GoogleBooksApi::class.java)
        }

        fun getBooksDao() : BooksDao {
            return RetrofitClient.getClient(BASE_URL).create(BooksDao::class.java)
        }


    }


}