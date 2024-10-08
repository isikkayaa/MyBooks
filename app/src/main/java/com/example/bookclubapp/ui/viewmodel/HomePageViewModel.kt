package com.example.bookclubapp.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookclubapp.data.entity.BooksResponse
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.data.entity.ImageLinks
import com.example.bookclubapp.data.entity.VolumeInfo
import com.example.bookclubapp.data.repository.BooksRepository
import com.example.bookclubapp.retrofit.ApiUtils
import com.example.bookclubapp.retrofit.GoogleBooksApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val brepo: BooksRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _kitaplarListesi = MutableLiveData<List<VolumeInfo>?>()
    val kitaplarListesi: LiveData<List<VolumeInfo>?> get() = _kitaplarListesi

    private val _favBooks = MutableLiveData<List<FavBooks>>()
    val favBooks: LiveData<List<FavBooks>> get() = _favBooks

    private val googleBooksApi: GoogleBooksApi = ApiUtils.getGoogleBooksApi()

    init {
        homepageKitaplariYukle()
    }

    fun homepageKitaplariYukle() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _kitaplarListesi.value = listOf(brepo.homepageKitapYukle())
                brepo.homepageKitapYukle()
            } catch (e: Exception) {

            }
        }
    }


    fun searchBooks(query: String, apiKey: String) {
        viewModelScope.launch {
            try {

                val response = googleBooksApi.searchBooks(query, apiKey)


                Log.d("HomePageViewModel", "API Yanıtı: $response")


                val books = response.items?.mapNotNull { bookItem ->
                    bookItem.volumeInfo?.let {
                        VolumeInfo(
                            title = it.title ?: "Bilinmeyen Başlık",
                            authors = it.authors ?: emptyList(),
                            description = it.description ?: "Açıklama yok",
                            imageLinks = it.imageLinks
                        )
                    }
                }

                _kitaplarListesi.postValue(books ?: emptyList())

                Log.d("HomePageViewModel", "Kitap listesi post edildi: ${books?.size} kitap")

            } catch (e: Exception) {
                Log.e("HomePageViewModel", "API'den kitap çekilirken hata oluştu", e)
            }
        }
    }



    fun addBookToFavorites(book: VolumeInfo, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val favoriteBook = hashMapOf(
            "title" to book.title,
            "authors" to book.authors,
            "description" to book.description,
            "thumbnail" to book.imageLinks?.thumbnail
        )

        db.collection("users").document(userId).collection("favorites")
            .document(book.title)
            .set(favoriteBook)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding book to favorites", e)
                onComplete(false)
            }
    }


    fun fetchFavoriteBooks() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                val books = result.map { document ->
                    FavBooks(
                        title = document.getString("title")!!,

                        authors = document.get("authors") as? List<String>,
                        description = document.getString("description"),
                        imageLinks = ImageLinks(
                            thumbnail = document.getString("thumbnail"),
                            smallThumbnail = null
                        )
                    )
                }
                _favBooks.postValue(books)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting favorite books", e)
            }
    }


    fun removeFromFavorites(title: String, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users").document(userId).collection("favorites")
            .document(title)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error removing book from favorites", e)
                onComplete(false)
            }
    }


    private fun <T> MutableLiveData<T>.postValue(booksResponse: BooksResponse) {

    }
}
