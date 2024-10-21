package com.example.bookclubapp.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookclubapp.data.entity.BooksResponse
import com.example.bookclubapp.data.entity.Comment
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.data.entity.ImageLinks
import com.example.bookclubapp.data.entity.NyTimesBook
import com.example.bookclubapp.data.entity.VolumeInfo
import com.example.bookclubapp.data.repository.BooksRepository
import com.example.bookclubapp.retrofit.ApiUtils
import com.example.bookclubapp.retrofit.GoogleBooksApi
import com.example.bookclubapp.retrofit.NyTimesApi
import com.example.bookclubapp.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

    private val _okunanBooks = MutableLiveData<List<VolumeInfo>?>()
    val okunanBooks: LiveData<List<VolumeInfo>?> get() = _okunanBooks



    // Okunan kitap sayısını tutacak olan LiveData
    private val _okunanBooksCount = MutableLiveData<Int>()
    val okunanBooksCount: LiveData<Int> get() = _okunanBooksCount

    private val _bestsellerBooks = MutableLiveData<List<VolumeInfo>?>()
    val bestsellerBooks : LiveData<List<VolumeInfo>?> get()  = _bestsellerBooks


    private val _readBooks = MutableLiveData<List<VolumeInfo>?>()
    val readBooks : LiveData<List<VolumeInfo>?> get()  = _readBooks

    private val _favoriteBooks = MutableLiveData<List<VolumeInfo>?>()
    val favoriteBooks : LiveData<List<VolumeInfo>?> get()  = _favoriteBooks



    private val _okuyacakBooks = MutableLiveData<List<VolumeInfo>?>()
    val okuyacakBooks : LiveData<List<VolumeInfo>?> get() = _okuyacakBooks

    private val googleBooksApi: GoogleBooksApi = ApiUtils.getGoogleBooksApi()
    private val nyTimesApi : NyTimesApi = ApiUtils.getNyTimesApi()

    init {
        homepageKitaplariYukle()
    }

    fun homepageKitaplariYukle() {
        viewModelScope.launch {
            try {
                val bestsellerBooks = brepo.getBestsellerBooks()
              /*  val readBooks = brepo.getReadBooks()
                val favoriteBooks = brepo.getFavoriteBooks()

               */

                _bestsellerBooks.value = bestsellerBooks
               // _bestsellerBooks.postValue(bestsellerBooks)
               // _readBooks.value = readBooks
                //_favoriteBooks.value = favoriteBooks
                delay(2000) // 2 saniye bekleme
            } catch (e: HttpException) {
                if (e.code() == 429) {
                    Log.e("HomePage", "Too many requests, please try again later.")
                } else {
                    Log.e("HomePage", "Error loading books", e)
                }
            }

        }

    }

    fun searchBooks(query: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = brepo.searchBooks(query)
                val books = response.mapNotNull { volumeItem ->
                    volumeItem.let {
                        VolumeInfo(
                            title = it.title ?: "Bilinmeyen Başlık",
                            authors = it.authors ?: emptyList(),  // Null ise boş liste
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


    fun addBookToReadList(book: VolumeInfo, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val okunanBook = hashMapOf(
            "title" to book.title,
            "authors" to book.authors,
            "thumbnail" to book.imageLinks?.thumbnail
        )

        db.collection("users").document(userId).collection("readBooks")
            .document(book.title)
            .set(okunanBook)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding book to readlist", e)
                onComplete(false)
            }
    }


    fun fetchokunanBooks() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).collection("readBooks")
            .get()
            .addOnSuccessListener { result ->
                val okunanbooks = result.map { document ->
                    VolumeInfo(
                        title = document.getString("title")!!,
                        authors = document.get("authors") as? List<String>,
                        imageLinks = ImageLinks(
                            thumbnail = document.getString("thumbnail"),
                            smallThumbnail = null
                        )
                    )
                }
                _okunanBooks.postValue(okunanbooks)
                Log.d("HomePageViewModel", "Okunan kitaplar: $okunanbooks")  // LOG EKLEYİN

                _okunanBooksCount.postValue(okunanbooks.size)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting read books", e)
            }
    }


    fun fetchOkuyacakBooks() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).collection("readingList")
            .get()
            .addOnSuccessListener { result ->
                val okunacakBooks = result.map { document ->
                    VolumeInfo(
                        title = document.getString("title")!!,
                        authors = document.get("authors") as? List<String>,
                        imageLinks = ImageLinks(
                            thumbnail = document.getString("thumbnail"),
                            smallThumbnail = null
                        )
                    )
                }
                _okuyacakBooks.postValue(okunacakBooks)
                Log.d("HomePageViewModel", "Okuyacak kitaplar: $okunacakBooks")  // LOG EKLEYİN
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting to-read books", e)
            }
    }

    fun addBookToReadingList(book: VolumeInfo, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val okuyacakBook = hashMapOf(
            "title" to book.title,
            "authors" to book.authors,
            "thumbnail" to book.imageLinks?.thumbnail
        )

        db.collection("users").document(userId).collection("readingList")
            .document(book.title)
            .set(okuyacakBook)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding book to readlist", e)
                onComplete(false)
            }
    }


    fun removeFromCurrentlyList(title: String, onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users").document(userId).collection("currentlyList")
            .document(title)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error removing book from favorites", e)
                onComplete(false)
            }
    }









}
