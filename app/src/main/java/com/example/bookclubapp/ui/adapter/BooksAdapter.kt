package com.example.bookclubapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookclubapp.R
import com.example.bookclubapp.data.entity.VolumeInfo
import com.example.bookclubapp.databinding.BooksTasarimBinding
import com.example.bookclubapp.databinding.CardTasarimBinding
import com.example.bookclubapp.ui.fragment.HomePageFragmentDirections
import com.example.bookclubapp.ui.fragment.ProfileFragmentDirections
import com.example.bookclubapp.ui.fragment.ReadFragmentDirections
import com.example.bookclubapp.ui.fragment.ReadingListFragmentDirections
import com.example.bookclubapp.util.gecisYap

class BooksAdapter(var mContext: Context,var kitaplarListesi: List<VolumeInfo>,var okunanKitaplarListesi: List<VolumeInfo>,
                   var okunacakKitaplarListesi: List<VolumeInfo>,var fragmentType: String) : RecyclerView.Adapter<BooksAdapter.BooksTasarimTutucu>() {

    inner class BooksTasarimTutucu(var tasarim : BooksTasarimBinding) : RecyclerView.ViewHolder(tasarim.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksTasarimTutucu {
        val binding: BooksTasarimBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.books_tasarim,parent,false)
        return BooksTasarimTutucu(binding)
    }


    override fun onBindViewHolder(holder: BooksTasarimTutucu, position: Int) {
        val kitap = kitaplarListesi[position]
        val t = holder.tasarim

        t.kitapNesnesi = kitap

        holder.tasarim.gorsel = kitap.imageLinks
        holder.tasarim.textView10.text = kitap.title

        val thumbnailUrl = kitap.imageLinks?.thumbnail
        if (thumbnailUrl != null) {
            Glide.with(holder.itemView.context)
                .load(thumbnailUrl)
                .placeholder(R.drawable.baseline_menu_book_24)
                .error(R.drawable.baseline_list_24)
                .into(holder.tasarim.imageViewKitapResim)
        } else {
            holder.tasarim.imageViewKitapResim.setImageResource(R.drawable.baseline_menu_book_24)
        }


        // Fragment tipine göre geçiş
        t.cardViewBooks.setOnClickListener {
            when (fragmentType) {
                "ReadFragment" -> {
                    val gecis = kitap.imageLinks?.let { it1 ->
                        ReadFragmentDirections.actionReadFragmentToBookDetailFragment(
                            kitap = kitap,
                            gorsel = it1
                        )
                    }
                    if (gecis != null) {
                        Navigation.gecisYap(it, gecis)
                    }
                }

                "ReadingListFragment" -> {
                    val geciss = kitap.imageLinks?.let { it2 ->
                        ReadingListFragmentDirections.actionReadingListFragmentToBookDetailFragment(
                            kitap = kitap,
                            gorsel = it2
                        )
                    }
                    if (geciss != null) {
                        Navigation.gecisYap(it, geciss)
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