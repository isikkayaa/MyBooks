package com.example.bookclubapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookclubapp.R
import com.example.bookclubapp.databinding.FragmentReadBinding
import com.example.bookclubapp.ui.adapter.BooksAdapter
import com.example.bookclubapp.ui.viewmodel.HomePageViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadFragment : Fragment() {

    private lateinit var binding: FragmentReadBinding
    private lateinit var adapter : BooksAdapter
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var viewModel: HomePageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel : HomePageViewModel by viewModels()
        viewModel = tempViewModel
        firebaseAuth = FirebaseAuth.getInstance()

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_read,container,false)
        viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)


        binding.fragment = this
        binding.viewmodel = viewModel

        binding.lifecycleOwner = this


        adapter = BooksAdapter(requireContext(), emptyList(), emptyList(), emptyList())
        binding.rvreadbooks.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        binding.rvreadbooks.adapter = adapter
        firebaseAuth = FirebaseAuth.getInstance()






        showReadBooks()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.okunanBooksCount.observe(viewLifecycleOwner) { count ->

            binding.textView14.text = "$count Books"
        }
        viewModel.fetchokunanBooks()

    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchokunanBooks()
    }


    private fun showReadBooks() {
        viewModel.okunanBooks.observe(viewLifecycleOwner, Observer { books ->
            if (books != null) {
                adapter.updateBooks(books)
            } else {
                Log.d("ReadFragment", "Okunan kitaplar listesi boş")
            }
        })
    }




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