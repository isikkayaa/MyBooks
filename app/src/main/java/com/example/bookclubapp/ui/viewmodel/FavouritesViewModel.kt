package com.example.bookclubapp.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookclubapp.data.entity.FavBookItem
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.data.entity.ImageLinks
import com.example.bookclubapp.data.repository.BooksRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(var brepo:BooksRepository,  @ApplicationContext private val context: Context
) : ViewModel() {
    var favKitaplarListesi = MutableLiveData<List<FavBooks>?>()
    private val _favBooks = MutableLiveData<List<FavBooks>>()
    val favBooks: LiveData<List<FavBooks>> get() = _favBooks

    fun fetchFavoriteBooks() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                val books = result.map { document ->
                    FavBooks(
                        title = document.getString("title")!!,
                        // authors'ı List<String> olarak almak için get("authors") kullanın
                        authors = document.get("authors") as? List<String>, // Burayı düzelttik
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

    init {
        favKitaplariYukle()
    }

    fun favKitaplariYukle() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val favBooksList = brepo.favKitaplariYukle() // Bu List<FavBooks> dönecek
                favKitaplarListesi.value = favBooksList
            } catch (e: Exception) {
                // Hata yönetimi
            }
        }
    }



}