package com.example.bookclubapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.bookclubapp.ui.viewmodel.UsersViewModel
import com.example.bookclubapp.R
import com.example.bookclubapp.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val usersViewModel: UsersViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonSignIn.setOnClickListener { a ->

            val userName = binding.usernameSignUp.text.toString()
            val email = binding.emailSignUp.text.toString()
            val pass = binding.passSignUp.text.toString()

            if (userName.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userId = firebaseAuth.currentUser?.uid

                        if (userId != null) {
                            // Kullanıcı Firestore'a kaydediliyor
                            usersViewModel.registerUser(userId, userName, email) { success ->
                                if (success) {
                                    Navigation.findNavController(a).navigate(R.id.signtologinGecis)
                                } else {
                                    Toast.makeText(requireContext(), "Error saving user data", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    } else {
                        Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Empty Fields Are Not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}
