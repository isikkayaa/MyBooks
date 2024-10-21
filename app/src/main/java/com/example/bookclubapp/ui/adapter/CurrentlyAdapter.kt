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
import com.example.bookclubapp.data.entity.CurrentlyBookList
import com.example.bookclubapp.data.entity.ImageLinks
import com.example.bookclubapp.data.entity.VolumeInfo
import com.example.bookclubapp.databinding.CardCurrentlyTasarimBinding
import com.example.bookclubapp.ui.fragment.ProfileFragmentDirections
import com.example.bookclubapp.ui.viewmodel.HomePageViewModel
import com.example.bookclubapp.ui.viewmodel.ProfileViewModel
import com.example.bookclubapp.util.gecisYap
import com.google.android.material.snackbar.Snackbar

class CurrentlyAdapter(var mContext: Context,var curenntlyBooksListesi: List<CurrentlyBookList>,var viewModel: HomePageViewModel
) : RecyclerView.Adapter<CurrentlyAdapter.CardCurrentlyTasarimTutucu>(){

     inner class CardCurrentlyTasarimTutucu(var tasarim: CardCurrentlyTasarimBinding) : RecyclerView.ViewHolder(tasarim.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCurrentlyTasarimTutucu {
        val binding: CardCurrentlyTasarimBinding  = DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.card_currently_tasarim,parent,false)
        return CardCurrentlyTasarimTutucu(binding)

    }


    override fun onBindViewHolder(holder: CardCurrentlyTasarimTutucu, position: Int) {
        val currentBook = curenntlyBooksListesi[position]
        val t = holder.tasarim


        t.currentBookNesnesi = currentBook

        holder.tasarim.textView19.text = currentBook.bookTitle
        holder.tasarim.textView20.text = currentBook.bookAuthors?.joinToString(" , ") ?: "Unknown Author"


        if (currentBook.bookImageUrl != null) {
            Glide.with(mContext)
                .load(currentBook.bookImageUrl)
                .placeholder(R.drawable.baseline_menu_book_24)
                .error(R.drawable.baseline_list_24)
                .into(holder.tasarim.imageView7)
        } else {
            holder.tasarim.imageView7.setImageResource(R.drawable.baseline_menu_book_24)
        }


        t.button2.setOnClickListener {
            viewModel.removeFromCurrentlyList(currentBook.bookTitle) { success ->
                if (success) {

                    // `CurrentlyBookList`'ten `VolumeInfo`'ya dönüştürme
                    val volumeInfo = VolumeInfo(
                        title = currentBook.bookTitle,
                        authors = currentBook.bookAuthors,
                        imageLinks = ImageLinks(
                            thumbnail = currentBook.bookImageUrl,  // Eğer sadece `bookImageUrl` varsa thumbnail olarak kullanıyoruz
                            smallThumbnail = null // Varsa başka bir alan da ekleyebilirsiniz
                        )
                    )

                    viewModel.addBookToReadList(volumeInfo) { success ->
                        if (success) {
                            Snackbar.make(it, "${currentBook.bookTitle} added to ReadList!", Snackbar.LENGTH_SHORT)
                                .show()
                            viewModel.fetchokunanBooks()
                        }
                    }
                }
            }




        }




        t.cardViewCurrently.setOnClickListener {
            // currentBook.bookImageUrl'u kullanarak ImageLinks nesnesini oluşturuyoruz
            val imageLinks = ImageLinks(
                thumbnail = currentBook.bookImageUrl,
                smallThumbnail = null  // Eğer küçük bir görsel URL'si yoksa null yapıyoruz
            )

            // VolumeInfo nesnesini oluşturuyoruz
            val volumeInfo = VolumeInfo(
                title = currentBook.bookTitle,
                authors = currentBook.bookAuthors,
                imageLinks = imageLinks  // ImageLinks nesnesini buraya ekliyoruz
                // Diğer gerekli alanları doldurun
            )

            // Geçiş nesnesini oluşturuyoruz
            val gecis = ProfileFragmentDirections.actionProfileFragmentToBookDetailFragment(
                kitap = volumeInfo,  // VolumeInfo nesnesini geçiriyoruz
                gorsel = imageLinks  // ImageLinks nesnesini geçiriyoruz
            )

            // Navigasyonu gerçekleştiriyoruz
            Navigation.gecisYap(it, gecis)
        }



    }


    override fun getItemCount(): Int {
        return curenntlyBooksListesi.size
    }


    fun updateBooks(newBooks: List<CurrentlyBookList>) {
        val oldList = curenntlyBooksListesi
        curenntlyBooksListesi = newBooks
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newBooks.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].bookTitle == newBooks[newItemPosition].bookTitle
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newBooks[newItemPosition]
            }
        }).dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun currentyListAdd(newCurrentBooks : List<CurrentlyBookList>?) {
        curenntlyBooksListesi = newCurrentBooks ?: emptyList()
        notifyDataSetChanged()
    }




}