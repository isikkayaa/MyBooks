package com.example.bookclubapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookclubapp.databinding.FragmentFavouritesBinding
import com.example.bookclubapp.retrofit.BooksDao
import com.example.bookclubapp.ui.adapter.FavouritesAdapter
import com.example.bookclubapp.ui.adapter.HomePageAdapter
import com.example.bookclubapp.ui.viewmodel.FavouritesViewModel
import com.example.bookclubapp.ui.viewmodel.HomePageViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
  //  private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var favouritesAdapter: FavouritesAdapter
    private lateinit var booksDao: BooksDao
    private lateinit var homePageViewModel: HomePageViewModel
    private lateinit var firebaseAuth: FirebaseAuth


    private lateinit var binding : FragmentFavouritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomePageViewModel by viewModels()
        homePageViewModel = tempViewModel
        firebaseAuth = FirebaseAuth.getInstance()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavouritesBinding.inflate(inflater,container,false)
        binding.fragment = this
        binding.viewModel= homePageViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homePageViewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)

       // homePageViewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)
        favouritesAdapter = FavouritesAdapter(requireContext(),emptyList(),homePageViewModel)

        binding.rvFav.adapter = favouritesAdapter
        setupRecyclerView()

        observeViewModel()


        homePageViewModel.favBooks.observe(viewLifecycleOwner) { it ->
            if (it != null) {
                favouritesAdapter.updateFavorites(it)
                homePageViewModel.fetchFavoriteBooks()
            }
        }

        // Favori kitapları Firestore'dan çek
        homePageViewModel.fetchFavoriteBooks()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homePageViewModel.fetchFavoriteBooks()
    }

    override fun onResume() {
        super.onResume()
        homePageViewModel.fetchFavoriteBooks()
    }


    private fun setupRecyclerView() {
        favouritesAdapter = FavouritesAdapter(requireContext(), emptyList(), homePageViewModel)
        binding.rvFav.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouritesAdapter
        }
    }

    private fun observeViewModel() {
        // Favori kitapları gözlemleme
        homePageViewModel.favBooks.observe(viewLifecycleOwner) { favBooks ->
            if (favBooks != null && favBooks.isNotEmpty()) {
                favouritesAdapter.updateFavorites(favBooks)  // Adapter'a yeni veriyi gönder
            } else {
                Log.d("FavouritesFragment", "Favori kitap listesi boş")
            }
        }
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