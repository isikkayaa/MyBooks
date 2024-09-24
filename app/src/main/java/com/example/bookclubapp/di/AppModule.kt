package com.example.bookclubapp.di

import com.example.bookclubapp.data.datasource.BookDataSource
import com.example.bookclubapp.data.repository.BooksRepository
import com.example.bookclubapp.retrofit.ApiUtils
import com.example.bookclubapp.retrofit.BooksDao
import com.example.bookclubapp.retrofit.GoogleBooksApi
import com.example.bookclubapp.retrofit.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBooksRepository(bds:BookDataSource) : BooksRepository{
        return BooksRepository(bds)
    }
    @Provides
    @Singleton
    fun provideBookDataSource(bdao: BooksDao) : BookDataSource{
        return BookDataSource(bdao)
    }
    @Provides
    @Singleton
    fun provideBooksDao() : BooksDao{
        return ApiUtils.getBooksDao()
    }


    @Provides
    @Singleton
    fun provideUsersDao() : UsersDao{
        return UsersDao()
    }

    @Provides
    @Singleton
    fun provideGoogleBooksApi(): GoogleBooksApi {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)
    }





}