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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val usersViewModel: UsersViewModel by viewModels()
    private lateinit var firebaseAnalytics : FirebaseAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())

        val method = "email"

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.METHOD, method)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)


        binding.buttonSignIn.setOnClickListener { a ->

            val username = binding.usernameSignUp.text.toString()
            val email = binding.emailSignUp.text.toString()
            val pass = binding.passSignUp.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userId = firebaseAuth.currentUser?.uid

                        if (userId != null) {

                            val userMap = hashMapOf(
                                "userId" to userId,
                                "username" to username,
                                "email" to email
                            )
                            FirebaseFirestore.getInstance().collection("users").document(userId)
                                .set(userMap).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
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
