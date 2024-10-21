package com.example.bookclubapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookclubapp.R
import com.example.bookclubapp.databinding.FragmentReadingListBinding
import com.example.bookclubapp.ui.adapter.BooksAdapter
import com.example.bookclubapp.ui.viewmodel.HomePageViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadingListFragment : Fragment() {

    private lateinit var binding : FragmentReadingListBinding
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_reading_list,container,false)
        viewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)

        binding.fragment = this
        binding.viewModel = viewModel

        binding.lifecycleOwner = this


        adapter = BooksAdapter(requireContext(), emptyList(), emptyList(), emptyList())
        binding.rvreadingBooks.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        binding.rvreadingBooks.adapter = adapter
        firebaseAuth = FirebaseAuth.getInstance()

        showToReadBooks()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.fetchOkuyacakBooks()

    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchOkuyacakBooks()
    }





    private fun showToReadBooks() {
        viewModel.okuyacakBooks.observe(viewLifecycleOwner) { books ->
            if (books != null) {
                adapter.updateBooks(books)
            } else {
                Log.d("ProfileFragment", "Okuyacak kitaplar listesi boş")
            }
        }
    }

}