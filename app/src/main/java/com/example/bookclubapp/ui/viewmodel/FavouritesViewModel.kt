package com.example.bookclubapp.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.data.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(var brepo:BooksRepository,  @ApplicationContext private val context: Context
) : ViewModel() {
    var favKitaplarListesi = MutableLiveData<List<FavBooks>?>()

    init {
    //    favKitaplariYukle()
    }

    /*fun favKitaplariYukle() {
        viewModelScope.launch {
            try {
                val favBooksList = brepo.favKitaplariYukle() // Repository'den veriyi çekiyoruz
                favKitaplarListesi.value = favBooksList // LiveData'yı güncelliyoruz
            } catch (e: Exception) {
                Log.e("FavouritesViewModel", "Favori kitaplar yüklenirken hata oluştu: $e")
            }
        }
    }


     */

}