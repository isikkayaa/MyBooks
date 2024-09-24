package com.example.bookclubapp.retrofit

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


class UsersDao {
    private val db = FirebaseFirestore.getInstance()

    fun createUser(userId: String, username: String, email: String, callback: (Boolean) -> Unit) {
        val user = hashMapOf(
            "username" to username,
            "email" to email
        )

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("Firestore", "User created successfully")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error creating user", e)
                callback(false)
            }
    }
}
