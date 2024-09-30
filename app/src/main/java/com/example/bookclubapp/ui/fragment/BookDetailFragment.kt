package com.example.bookclubapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bookclubapp.R
import com.example.bookclubapp.data.entity.ImageLinks
import com.example.bookclubapp.data.entity.VolumeInfo
import com.example.bookclubapp.databinding.FragmentBookDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailBinding

    private lateinit var gelenKitap: VolumeInfo
    private lateinit var gelenGorsel: ImageLinks
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_book_detail,container,false)

        binding.bookDetailFragment = this

        val bundle : BookDetailFragmentArgs by navArgs()
        gelenKitap = bundle.kitap


        binding.kitapNesnesi = gelenKitap

        val bundle1:BookDetailFragmentArgs by navArgs()
        gelenGorsel = bundle1.gorsel
        binding.gorselNesnesi = gelenGorsel

        val thumbnailUrl = gelenGorsel.thumbnail
        if (thumbnailUrl != null) {
            context?.let {
                Glide.with(it)
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.baseline_menu_book_24)
                    .error(R.drawable.baseline_list_24)
                    .into(binding.imageViewKitapDetayresim)
            }
        } else {
            binding.imageViewKitapDetayresim.setImageResource(R.drawable.baseline_menu_book_24)
        }





        return binding.root
    }

    companion object {
        @JvmStatic
        @BindingAdapter("bindAuthors")
        fun bindAuthors(textView: TextView, authors: List<String>?) {
            textView.text = authors?.joinToString(", ") ?: "Bilinmeyen Yazar"
        }

        @JvmStatic
        @BindingAdapter("bindDescription")
        fun bindDescription(textView: TextView, description: String?) {
            textView.text = description ?: "Description not available"
        }
    }


}