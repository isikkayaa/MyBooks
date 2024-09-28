package com.example.bookclubapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookclubapp.data.entity.FavBooks
import com.example.bookclubapp.ui.viewmodel.FavouritesViewModel
import com.example.bookclubapp.R
import com.example.bookclubapp.data.entity.FavBookItem
import com.example.bookclubapp.databinding.CardFavTasarimBinding

class FavouritesAdapter (var mContext: Context, var favKitaplarListesi : List<FavBooks>, var viewModel : FavouritesViewModel) : RecyclerView.Adapter<FavouritesAdapter.CardTasarimTutucuFav>(){

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
    }
    override fun getItemCount(): Int {
        return favKitaplarListesi.size
    }

    fun updateFavorites(newFavorites: List<FavBooks>?) {
        if (newFavorites != null) {
            favKitaplarListesi = newFavorites
            notifyDataSetChanged()

        }
    }

}