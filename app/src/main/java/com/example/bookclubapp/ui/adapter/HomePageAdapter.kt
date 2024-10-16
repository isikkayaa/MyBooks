package com.example.bookclubapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
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
    var okunanKitaplarListesi: List<VolumeInfo>,
    var okunacakKitaplarListesi: List<VolumeInfo>,
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

        t.kitapNesnesi = kitap


        // Favori durumunu kontrol etme
        val isFavourited = favKitaplar.any { it.title == kitap.title }

        // Eğer favoriyse kalbi dolu göster, değilse boş kalp göster
        if (isFavourited) {
            t.imageViewkalpborder.setImageResource(R.drawable.baseline_favorite_24) // Dolu kalp
        } else {
            t.imageViewkalpborder.setImageResource(R.drawable.baseline_favorite_border_24) // Boş kalp
        }

        holder.tasarim.imageView5.setOnClickListener {
            // SharedPreferences()
            //readBooks(okunanKitaplarListesi)

            viewModel.addBookToReadList(kitap) { success ->
                if (success) {
                    Snackbar.make(it, "${kitap.title} Kitaplarım'a eklendi!", Snackbar.LENGTH_SHORT)
                        .show()
                    viewModel.fetchokunanBooks()

                    readBooks(okunanKitaplarListesi)

                }
            }


        }


        holder.tasarim.imageViewList.setOnClickListener {
            viewModel.addBookToReadingList(kitap) { success ->
                if (success) {
                    Snackbar.make(
                        it,
                        "${kitap.title} Okuyacaklarım'a eklendi!",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    readingBooks(okunacakKitaplarListesi)


                }

            }
        }

        holder.tasarim.gorsel = kitap.imageLinks
        holder.tasarim.kitapNesnesi = kitap


        holder.tasarim.textView8.text = kitap.title
        holder.tasarim.textView9.text = kitap.authors?.joinToString(" , ") ?: "Unknown Author"


        val thumbnailUrl = kitap.imageLinks?.thumbnail

        if (thumbnailUrl != null && thumbnailUrl.isNotBlank()) {
            Glide.with(holder.itemView.context)
                .load(thumbnailUrl)
                .placeholder(R.drawable.baseline_menu_book_24)
                .error(R.drawable.baseline_list_24)
                .into(holder.tasarim.imageViewKitapGorsel)
        } else {
            holder.tasarim.imageViewKitapGorsel.setImageResource(R.drawable.baseline_menu_book_24)
        }






        t.cardViewSatir.setOnClickListener {
            val gecis = kitap.imageLinks?.let { it1 ->
                HomePageFragmentDirections.detayGecis(
                    kitap = kitap,
                    gorsel = it1
                )
            }
            if (gecis != null) {
                Navigation.gecisYap(it, gecis)

            }
        }

        //val url = "http://www.googleapis.com/books/v1/}"
        //Glide.with(mContext).load(url).override(500,750).into(t.imageViewKitapGorsel)

        // Kitap favori durumunu değiştiren buton işlevi
        t.imageViewkalpborder.setOnClickListener {
            if (isFavourited) {
                viewModel.removeFromFavorites(kitap.title) { success ->
                    if (success) {
                        t.imageViewkalpborder.setImageResource(R.drawable.baseline_favorite_border_24)
                        Snackbar.make(
                            it,
                            "${kitap.title} favorilerden kaldırıldı!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(it, "Kaldırma işlemi başarısız", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } else {
                viewModel.addBookToFavorites(kitap) { success ->
                    if (success) {
                        t.imageViewkalpborder.setImageResource(R.drawable.baseline_favorite_24)
                        Snackbar.make(
                            it,
                            "${kitap.title} favorilere eklendi!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(it, "Ekleme işlemi başarısız", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



        override fun getItemCount(): Int {
        return kitaplarListesi.size
    }


    fun updateBooks(newBooks: List<VolumeInfo>) {
        val oldList = kitaplarListesi
        kitaplarListesi = newBooks
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newBooks.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].title == newBooks[newItemPosition].title
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newBooks[newItemPosition]
            }
        }).dispatchUpdatesTo(this)
    }




    @SuppressLint("NotifyDataSetChanged")
    fun updateFavorites(newFavorites: List<FavBooks>) {
        favKitaplar = newFavorites
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun readBooks(okunanKitaplar: List<VolumeInfo>) {
        okunanKitaplarListesi = okunanKitaplar
        notifyDataSetChanged()  // Veri güncellendiğinde adapter'ı yenile
    }

    @SuppressLint("NotifyDataSetChanged")
    fun readingBooks(okunacakKitaplar: List<VolumeInfo>) {
        okunacakKitaplarListesi = okunacakKitaplar
        notifyDataSetChanged()  // Veri güncellendiğinde adapter'ı yenile
    }

}
