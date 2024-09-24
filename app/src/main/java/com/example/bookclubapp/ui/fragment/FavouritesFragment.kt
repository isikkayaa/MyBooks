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
    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var favouritesAdapter: FavouritesAdapter
    private lateinit var booksDao: BooksDao
    private lateinit var homePageViewModel: HomePageViewModel
    private lateinit var firebaseAuth: FirebaseAuth


    private lateinit var binding : FragmentFavouritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: FavouritesViewModel by viewModels()
        favouritesViewModel = tempViewModel
        firebaseAuth = FirebaseAuth.getInstance()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavouritesBinding.inflate(inflater,container,false)
        binding.fragment = this
        binding.viewModel= favouritesViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        homePageViewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)

        favouritesViewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)
        favouritesAdapter = FavouritesAdapter(requireContext(),emptyList(),favouritesViewModel)

        binding.rvFav.adapter = favouritesAdapter
        setupRecyclerView()

        observeViewModel()


        // Favori kitapları gözlemleme
        favouritesViewModel.favKitaplarListesi.observe(viewLifecycleOwner) { it ->
            if (it != null) {
                favouritesAdapter.updateFavorites(it)
            }
        }

        // Favori kitapları Firestore'dan çek
        homePageViewModel.fetchFavoriteBooks()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouritesViewModel.favKitaplariYukle()
    }

    override fun onResume() {
        super.onResume()
        favouritesViewModel.favKitaplariYukle()
    }


    private fun setupRecyclerView() {
        favouritesAdapter = FavouritesAdapter(requireContext(), emptyList(), favouritesViewModel)
        binding.rvFav.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouritesAdapter
        }
    }

    private fun observeViewModel() {
        favouritesViewModel.favKitaplarListesi.observe(viewLifecycleOwner) {it ->
            it?.let {
                if (it.isNotEmpty()) {
                    favouritesAdapter.updateFavorites(it)
                }else {
                    Log.d("FavouritesFragment","listebos")
                } ?: run {
                    Log.d("Fav", "Kitaplar listesi null")
                }
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