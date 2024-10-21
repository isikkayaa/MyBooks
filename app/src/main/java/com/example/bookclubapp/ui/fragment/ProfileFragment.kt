package com.example.bookclubapp.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.bookclubapp.ui.viewmodel.ProfileViewModel
import com.example.bookclubapp.R
import com.example.bookclubapp.databinding.FragmentProfileBinding
import com.example.bookclubapp.ui.adapter.BooksAdapter
import com.example.bookclubapp.ui.adapter.CommentsAdapter
import com.example.bookclubapp.ui.adapter.CurrentlyAdapter
import com.example.bookclubapp.ui.adapter.HomePageAdapter
import com.example.bookclubapp.ui.viewmodel.HomePageViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var viewModel : ProfileViewModel
    private lateinit var adapter : BooksAdapter
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var currentlyAdapter: CurrentlyAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var kontrol = false
    private lateinit var homePageViewModel : HomePageViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ProfileViewModel by viewModels()
        viewModel = tempViewModel
        firebaseAuth = FirebaseAuth.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        homePageViewModel = ViewModelProvider(this).get(HomePageViewModel::class.java)


        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        binding.homePageViewModel = homePageViewModel



        commentsAdapter = CommentsAdapter(requireContext(), emptyList(), emptyList(), emptyList())
        binding.recyclerViewComments.layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewComments.adapter = commentsAdapter
        firebaseAuth = FirebaseAuth.getInstance()



        currentlyAdapter = CurrentlyAdapter(requireContext(), emptyList(),homePageViewModel)
        binding.recyclerViewCurrently.layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL)
        binding.recyclerViewCurrently.adapter = currentlyAdapter

        viewModel.loadProfileData()


        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    viewModel.profileImageUri.value = it
                    viewModel.uploadProfileImage(it)
                }
            }
        }


        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                openImageChooser()
            } else {

            }
        }


        binding.buttonUpload.setOnClickListener {
            openImageChooser()
        }





        viewModel.fetchComments()
        viewModel.commentsList.observe(viewLifecycleOwner) { comments ->
            comments?.let {

                commentsAdapter.submitList(it)
            }
        }

        viewModel.fetchCurrentlyList()
        viewModel.currentlyList.observe(viewLifecycleOwner){currentBooks ->
            currentBooks?.let {
                currentlyAdapter.currentyListAdd(it)
            }
        }



        return binding.root

    }


    override fun onResume() {
        super.onResume()

    }





    fun onUploadButtonClick() {
        checkPermissionAndOpenImageChooser()
    }




    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
       // viewModel.saveProfileData("")
        viewModel.loadProfileData()

    }

    fun checkPermissionAndOpenImageChooser() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                openImageChooser()
            }
            shouldShowRequestPermissionRationale(permission) -> {

                permissionLauncher.launch(permission)
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }



    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001




        @JvmStatic
        @BindingAdapter("imageUri")
        fun loadImage(imageView: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(imageView.context)
                    .load(url)
                    .into(imageView)
            }
        }


        @JvmStatic
        @BindingAdapter("bindAuthors")
        fun bindAuthors(textView: TextView, authors: List<String>?) {
            textView.text = authors?.joinToString(", ") ?: "Bilinmeyen Yazar"
        }




    }
}





