package com.example.bookclubapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookclubapp.R
import com.example.bookclubapp.databinding.FragmentSearchBinding
import com.example.bookclubapp.ui.adapter.HomePageAdapter
import com.example.bookclubapp.ui.viewmodel.HomePageViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var homePageViewModel: HomePageViewModel
    private lateinit var homePageAdapter: HomePageAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomePageViewModel by viewModels()
        homePageViewModel = tempViewModel
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_search,container,false)

        homePageAdapter = HomePageAdapter(requireContext(), emptyList(), emptyList(),
            emptyList(), emptyList(),homePageViewModel)

        binding.fragment = this
        binding.viewModel = homePageViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()


        binding.adapter = homePageAdapter

        observeViewModel()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    homePageViewModel.searchBooks(query, "AIzaSyCfkKjXimNQjMHWWWInqTqHwZWc8MLavN4")
                    Log.d("HomePageFragment", "Arama yapılıyor: $query")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false


            }
        })







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
        homePageAdapter = HomePageAdapter(requireContext(), emptyList(), emptyList(),
            emptyList(), emptyList(),homePageViewModel)


        binding.rvSearchResults.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = homePageAdapter

        }
    }


    private fun observeViewModel() {
        homePageViewModel.kitaplarListesi.observe(viewLifecycleOwner) { kitaplarListesi ->
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