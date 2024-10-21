package com.example.bookclubapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookclubapp.R
import com.example.bookclubapp.data.entity.Comment
import com.example.bookclubapp.data.entity.VolumeInfo
import com.example.bookclubapp.databinding.CardCommentsTasarimBinding

class CommentsAdapter(var mContext: Context,var yorumListesi : List<Comment>,var yorumlananKitaplarListesi: List<Comment>,var comments:List<Comment>) : RecyclerView.Adapter<CommentsAdapter.CardCommentsTasarimTutucu>() {


    inner class CardCommentsTasarimTutucu(var tasarim : CardCommentsTasarimBinding) : RecyclerView.ViewHolder(tasarim.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCommentsTasarimTutucu {
        val binding : CardCommentsTasarimBinding =  DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.card_comments_tasarim,parent,false)
        return CardCommentsTasarimTutucu(binding)
    }

    override fun onBindViewHolder(holder: CardCommentsTasarimTutucu, position: Int) {
        val yorum = yorumListesi[position]
        val t = holder.tasarim

        t.yorumNesnesi = yorum


      //  holder.tasarim.gorsel = yorum.imageLinks?????

        holder.tasarim.textView16.text = yorum.bookTitle

        holder.tasarim.textView17.text = yorum.userComment

        if (yorum.bookImageUrl != null) {
            Glide.with(mContext)
                .load(yorum.bookImageUrl)
                .placeholder(R.drawable.baseline_menu_book_24)
                .error(R.drawable.baseline_list_24)
                .into(holder.tasarim.imageView2)
        } else {
            holder.tasarim.imageView2.setImageResource(R.drawable.baseline_menu_book_24)
        }


    }


    override fun getItemCount(): Int {
        return yorumListesi.size
    }




    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newComments: List<Comment>?) {
        yorumListesi = newComments ?: emptyList()
        notifyDataSetChanged() // Refresh the RecyclerView
    }


    fun updateBooks(newBooks: List<Comment>) {
        val oldList = yorumListesi
        yorumListesi = newBooks
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
    fun commentBooks(yorumlananKitaplar : List<Comment>) {
        yorumlananKitaplarListesi = yorumlananKitaplar
        notifyDataSetChanged()
    }






}