package com.example.bookclubapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.ui.viewmodel.FavouritesViewModel
import com.example.bookclubapp.R
import com.example.bookclubapp.databinding.CardFavTasarimBinding
import com.example.bookclubapp.ui.viewmodel.HomePageViewModel
import com.example.bookclubapp.util.gecisYap
import com.google.android.material.snackbar.Snackbar

class FavouritesAdapter (var mContext: Context, var favKitaplarListesi : List<FavBooks>, var viewModel : HomePageViewModel) : RecyclerView.Adapter<FavouritesAdapter.CardTasarimTutucuFav>(){

    inner class CardTasarimTutucuFav(var tasarim:CardFavTasarimBinding) : RecyclerView.ViewHolder(tasarim.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucuFav {
        val binding: CardFavTasarimBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.card_fav_tasarim,parent,false)
        return CardTasarimTutucuFav(binding)
    }




    override fun onBindViewHolder(holder: CardTasarimTutucuFav, position: Int) {
        val favKitap = favKitaplarListesi[position]
        val t = holder.tasarim


        t.favKitapNesnesi = favKitap

        val isRemoved = favKitaplarListesi.any {it.title == favKitap.title}

        if(isRemoved) {
            t.imageViewfavkitapheart.setImageResource(R.drawable.baseline_favorite_24)

        }else{

            t.imageViewfavkitapheart.setImageResource(R.drawable.baseline_favorite_border_24)
        }

        holder.tasarim.textViewfavKitapAd.text = favKitap.title

        val thumbnailUrl = favKitap.imageLinks?.thumbnail
        if(thumbnailUrl != null){
            Glide.with(holder.itemView.context)
                .load(thumbnailUrl)
                .into(holder.tasarim.imageViewfavKitap)
        }else {
            holder.tasarim.imageViewfavKitap.setImageResource(R.drawable.baseline_menu_book_24)

        }

        /*
        t.cardViewFav.setOnClickListener {
            val geciss = FavouritesFragmentDirections.actionFavouritesFragmentToBookDetailFragment(favKitap = favKitap)
            Navigation.gecisYap(it,geciss)

        }




         */



        t.imageViewfavkitapheart.setOnClickListener {
            if(isRemoved) {
                viewModel.removeFromFavorites(favKitap.title) { success ->
                    if (success) {
                        t.imageViewfavkitapheart.setImageResource(R.drawable.baseline_favorite_border_24)
                        Snackbar.make(
                            it,
                            "${favKitap.title} favorilerden kaldırıldı!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(it, "Kaldırma işlemi başarısız", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } else {

            }
        }
    }
    override fun getItemCount(): Int {
        return favKitaplarListesi.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavorites(newFavorites: List<FavBooks>?) {
        if (newFavorites != null) {
            favKitaplarListesi = newFavorites
            notifyDataSetChanged()

        }
    }

}