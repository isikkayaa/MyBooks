package com.example.bookclubapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookclubapp.data.entity.BookItem
import com.example.bookclubapp.retrofit.GoogleBooksApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val googleBooksApi: GoogleBooksApi // Hilt ile API'yi enjekte edin
) : ViewModel() {

    private val _books = MutableLiveData<List<BookItem>>()
    val books: LiveData<List<BookItem>> get() = _books

    fun searchBooks(query: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = googleBooksApi.searchBooks(query, apiKey)

                // Gelen yanıtı _books LiveData'sına ekleyin
                _books.postValue(response.items ?: emptyList())

                // Logları kontrol edin
                Log.d("BooksViewModel", "Fetched books: ${response.items?.size}")
            } catch (e: Exception) {
                Log.e("BooksViewModel", "Error fetching books", e)
            }
        }
    }
}
