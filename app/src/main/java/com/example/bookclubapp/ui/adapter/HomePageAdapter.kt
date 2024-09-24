package com.example.bookclubapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.ui.fragment.HomePageFragmentDirections
import com.example.bookclubapp.ui.viewmodel.HomePageViewModel
import com.example.bookclubapp.util.gecisYap
import com.example.bookclubapp.R
import com.example.bookclubapp.data.entity.VolumeInfo
import com.example.bookclubapp.databinding.CardTasarimBinding
import com.google.android.material.snackbar.Snackbar

class HomePageAdapter(
    var mContext: Context,
    var kitaplarListesi: List<VolumeInfo>,
    var favKitaplar: List<FavBooks>,
    var viewModel: HomePageViewModel
) : RecyclerView.Adapter<HomePageAdapter.CardTasarimTutucu>() {

    inner class CardTasarimTutucu(var tasarim: CardTasarimBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding: CardTasarimBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.card_tasarim, parent, false
        )
        return CardTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val kitap = kitaplarListesi[position]
        val t = holder.tasarim

        // Kitap nesnesi için binding
        t.kitapNesnesi = kitap


        // Favori durumu kontrolü
        val isFavourited = favKitaplar.any { it.title == kitap.title }
        if (isFavourited) {
            t.imageViewkalpborder.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            t.imageViewkalpborder.setImageResource(R.drawable.baseline_favorite_border_24)
        }


        holder.tasarim.gorsel = kitap.imageLinks
        holder.tasarim.kitapNesnesi = kitap

        // Kitap bilgilerini doldurma
        holder.tasarim.textView8.text = kitap.title
        holder.tasarim.textView9.text = kitap.authors?.joinToString(" , ") ?: "Unknown Author"


        val thumbnailUrl = kitap.imageLinks?.thumbnail
        if (thumbnailUrl != null) {
            Glide.with(holder.itemView.context)
                .load(thumbnailUrl)
                .placeholder(R.drawable.baseline_menu_book_24) // Yükleme sırasında gösterilecek bir placeholder
                .error(R.drawable.baseline_list_24) // Görsel yüklenemediğinde gösterilecek bir görsel
                .into(holder.tasarim.imageViewKitapGorsel)
        } else {
            holder.tasarim.imageViewKitapGorsel.setImageResource(R.drawable.baseline_menu_book_24)
        }


        // Kart tıklaması ile kitap detayına geçiş
        t.cardViewSatir.setOnClickListener {
            val gecis = kitap.imageLinks?.let { it1 -> HomePageFragmentDirections.detayGecis(kitap = kitap, gorsel = it1) }
            if (gecis != null) {
                Navigation.gecisYap(it, gecis)
            }
        }

        //val url = "http://www.googleapis.com/books/v1/}"
        //Glide.with(mContext).load(url).override(500,750).into(t.imageViewKitapGorsel)

        // Favorilere ekleme/kaldırma işlemi
        t.imageViewkalpborder.setOnClickListener {
            if (isFavourited) {
                // Favorilerden kaldırma işlemi
                viewModel.removeFromFavorites(kitap.title) { success ->
                    if (success) {
                        t.imageViewkalpborder.setImageResource(R.drawable.baseline_favorite_border_24)
                        Snackbar.make(it, "${kitap.title} favorilerden kaldırıldı!", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(it, "Kaldırma işlemi başarısız!", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Favorilere ekleme işlemi
                viewModel.addBookToFavorites(kitap) { success ->
                    if (success) {
                        t.imageViewkalpborder.setImageResource(R.drawable.baseline_favorite_24)
                        Snackbar.make(it, "${kitap.title} favorilere eklendi!", Snackbar.LENGTH_SHORT).show()
                        updateFavorites(favKitaplar)
                    } else {
                        Snackbar.make(it, "Ekleme işlemi başarısız!", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return kitaplarListesi.size
    }

    // Kitap listesini güncelleyen fonksiyon
    fun updateBooks(newBooks: List<VolumeInfo>) {
        kitaplarListesi = newBooks
        notifyDataSetChanged()
    }

    // Favori kitap listesini güncelleyen fonksiyon
    fun updateFavorites(newFavorites: List<FavBooks>) {
        favKitaplar = newFavorites
        notifyDataSetChanged()
    }
}
