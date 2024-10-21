package com.example.bookclubapp.di

import com.example.bookclubapp.data.datasource.BookDataSource
import com.example.bookclubapp.data.repository.BooksRepository
import com.example.bookclubapp.retrofit.ApiUtils
import com.example.bookclubapp.retrofit.BooksDao
import com.example.bookclubapp.retrofit.GoogleBooksApi
import com.example.bookclubapp.retrofit.NyTimesApi
import com.example.bookclubapp.retrofit.UsersDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
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
    fun provideBookDataSource(
        bdao: BooksDao,
        googleBooksApi: GoogleBooksApi,
        nyTimesApi: NyTimesApi
    ): BookDataSource {
        return BookDataSource(bdao, googleBooksApi, nyTimesApi)
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

    @Provides
    @Singleton
    fun provideNyTimesApi(): NyTimesApi {
        return Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/books/v3/lists/current/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NyTimesApi::class.java)

    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage() : FirebaseStorage {
        return FirebaseStorage.getInstance()
    }



}