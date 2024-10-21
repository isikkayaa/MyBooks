package com.example.bookclubapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView

import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.bookclubapp.retrofit.BooksDao
import com.example.bookclubapp.ui.adapter.HomePageAdapter
import com.example.bookclubapp.ui.viewmodel.HomePageViewModel
import com.example.bookclubapp.R
import com.example.bookclubapp.databinding.FragmentHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var homePageViewModel: HomePageViewModel
    private lateinit var homePageAdapter: HomePageAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var booksDao: BooksDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomePageViewModel by viewModels()
        homePageViewModel = tempViewModel
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false)

        homePageAdapter = HomePageAdapter(
            requireContext(), emptyList(), emptyList(),
            emptyList(), emptyList(), homePageViewModel
        )

        binding.homePageFragment = this
        binding.viewModel = homePageViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()


        binding.adapter = homePageAdapter

        observeViewModel()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homePageViewModel.homepageKitaplariYukle()
    }

    override fun onResume() {
        super.onResume()
        homePageViewModel.homepageKitaplariYukle()
    }

    private fun setupRecyclerView() {
        homePageAdapter = HomePageAdapter(
            requireContext(), emptyList(), emptyList(),
            emptyList(), emptyList(), homePageViewModel
        )
        binding.rvBestsellers.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            adapter = homePageAdapter
        }
       /* binding.rvReadBooks.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            adapter = homePageAdapter

        }

        binding.rvFavbooks.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            adapter = homePageAdapter
        }

        */


    }


    private fun observeViewModel() {
        homePageViewModel.bestsellerBooks.observe(viewLifecycleOwner, Observer { books ->
            if (books != null) {
                // Adapter'a kitapları yükleyin
                homePageAdapter.updateBooks(books)
            }
        })

        /*homePageViewModel.kitaplarListesi.observe(viewLifecycleOwner) { kitaplarListesi ->
            kitaplarListesi?.let {
                if (it.isNotEmpty()) {
                    homePageAdapter.updateBooks(it)
                } else {
                    Log.d("HomePageFragment", "Kitaplar listesi boş")
                }
            } ?: run {
                Log.d("HomePageFragment", "Kitaplar listesi null")
            }
        }

         */
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


        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, imageUrl: String?) {
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_menu_book_24) // Varsayılan görsel
                    .error(R.drawable.baseline_list_24) // Hata durumunda gösterilecek görsel
                    .into(view)
            } else {
                view.setImageResource(R.drawable.baseline_menu_book_24) // Eğer resim yoksa varsayılan görseli göster
            }
        }

    }
}




