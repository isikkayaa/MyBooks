package com.example.bookclubapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bookclubapp.R
import com.example.bookclubapp.data.entity.ImageLinks
import com.example.bookclubapp.data.entity.VolumeInfo
import com.example.bookclubapp.databinding.FragmentBookDetailBinding
import com.example.bookclubapp.ui.adapter.CommentsAdapter
import com.example.bookclubapp.ui.viewmodel.HomePageViewModel
import com.example.bookclubapp.ui.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Comment

@AndroidEntryPoint
class BookDetailFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailBinding

    private lateinit var gelenKitap: VolumeInfo
    private lateinit var gelenGorsel: ImageLinks

    private lateinit var viewModel : HomePageViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var yorumListesi : Comment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomePageViewModel by viewModels()
        viewModel = tempViewModel
        firebaseAuth = FirebaseAuth.getInstance()


    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_book_detail,container,false)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        binding.bookDetailFragment = this
        binding.viewModel = viewModel

        val bundle : BookDetailFragmentArgs by navArgs()
        gelenKitap = bundle.kitap

        binding.profilViewModel = profileViewModel



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


        binding.imageView3.setOnClickListener {
            viewModel.addBookToReadList(gelenKitap) { success ->
                if (success) {
                    Snackbar.make(it, "${gelenKitap.title} Kitaplarım'a eklendi!", Snackbar.LENGTH_SHORT)
                        .show()
                    viewModel.fetchokunanBooks()

                   // readBooks(okunanKitaplarListesi)

                }
            }

        }

        binding.imageView6.setOnClickListener {
            val editText = EditText(context)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            editText.layoutParams = layoutParams

            context?.let { context ->
                MaterialAlertDialogBuilder(context)
                    .setTitle(gelenKitap.title)
                    .setMessage("Write thoughts")
                    .setView(editText)
                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which -> }
                    .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                        val yorum = editText.text.toString()
                        val imageUrl = gelenGorsel.thumbnail // Kitap görsel URL'sini alıyoruz
                        profileViewModel.saveComment(gelenKitap, yorum, imageUrl)
                    }
                    .show()
            }
        }





        binding.imageView4.setOnClickListener {
            viewModel.addBookToReadingList(gelenKitap) { success ->
                if (success) {
                    Snackbar.make(
                        it,
                        "${gelenKitap.title} Okuyacaklarım'a eklendi!",
                        Snackbar.LENGTH_SHORT
                    ).show()

                   // readingBooks(okunacakKitaplarListesi)


                }

            }
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