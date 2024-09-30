package com.example.bookclubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookclubapp.retrofit.UsersDao
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val usersDao: UsersDao) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult


    fun registerUser(userId: String, username: String, email: String, callback: (Boolean) -> Unit) {
        usersDao.createUser(userId, username, email) { success ->
            callback(success)
        }
    }


    fun signInWithEmail(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            _loginResult.postValue(task.isSuccessful)
        }
    }


    fun signInWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            _loginResult.postValue(task.isSuccessful)
        }
    }


    fun signOut() {
        firebaseAuth.signOut()
        _loginResult.postValue(false)
    }
}
