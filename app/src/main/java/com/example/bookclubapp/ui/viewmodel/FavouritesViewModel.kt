package com.example.bookclubapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookclubapp.data.entity.FavBookItem
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.data.repository.BooksRepository
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