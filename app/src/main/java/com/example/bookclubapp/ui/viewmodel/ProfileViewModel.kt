package com.example.bookclubapp.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookclubapp.data.entity.Comment
import com.example.bookclubapp.data.entity.CurrentlyBookList
import com.example.bookclubapp.data.entity.VolumeInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel  @Inject constructor(@ApplicationContext private val context: Context, private val firebaseAuth: FirebaseAuth,private val firebaseStorage: FirebaseStorage) : ViewModel() {


    val userName = MutableLiveData<String>()
    val profileImageUri = MutableLiveData<Uri>()
    private val sharedPreferences =
        context.getSharedPreferences("BookClubApp", Context.MODE_PRIVATE)

    private val _commentsList = MutableLiveData<List<Comment>?>()
    val commentsList: LiveData<List<Comment>?> get() = _commentsList


    private val _currentlyList = MutableLiveData<List<CurrentlyBookList>?>()
    val currentlyList : LiveData<List<CurrentlyBookList>?> get() = _currentlyList

    private val firestore = FirebaseFirestore.getInstance()

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let {
            firestore.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userName.value = document.getString("username") ?: "User"
                        val imageUriString = sharedPreferences.getString("profile_image_uri", null)
                        profileImageUri.value = imageUriString?.let { Uri.parse(it) }
                    }
                }
                .addOnFailureListener {
                    Log.e("ProfileViewModel", "Error loading user data", it)
                }
        }
    }

    fun saveProfileData(name: String) {


        profileImageUri.value?.let { uri ->
            sharedPreferences.edit().putString("profile_image_uri", uri.toString()).apply()
        }
    }



    fun uploadProfileImage(uri: Uri) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val storageRef = firebaseStorage.reference.child("profile_images/$userId.jpg")

        storageRef.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                profileImageUri.value = downloadUrl
                saveProfileImageToFirestore(downloadUrl.toString())
            }
        }.addOnFailureListener {
            Log.e("ProfileViewModel", "Error uploading image", it)
        }
    }

    private fun saveProfileImageToFirestore(imageUrl: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val userDocRef = firestore.collection("users").document(userId)

        userDocRef.update("profileImageUri", imageUrl)
            .addOnSuccessListener {
                Log.d("ProfileViewModel", "Profile image URL updated successfully in Firestore.")
            }
            .addOnFailureListener {
                Log.e("ProfileViewModel", "Error updating profile image URL in Firestore", it)
            }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("imageUri")
        fun loadImage(imageView: ImageView, uri: MutableLiveData<Uri>?) {
            uri?.value?.let { imageView.setImageURI(it) }
        }
    }

    fun onUploadButtonClick() {

    }


    fun saveComment(book: VolumeInfo, comment: String, imageUrl: String?) {
        val newComment = hashMapOf(
            "bookTitle" to book.title,
            "userComment" to comment,
            "bookImageUrl" to imageUrl // Eğer imageUrl varsa
        )

        firestore.collection("comments")
            .add(newComment)
            .addOnSuccessListener {
                Log.d("ProfileViewModel", "Yorum başarıyla kaydedildi.")
            }
            .addOnFailureListener { e ->
                Log.e("ProfileViewModel", "Yorum kaydedilemedi: ", e)
            }
    }


    fun fetchComments() {
        firestore.collection("comments")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("ProfileViewModel", "Yorumları getirirken hata oluştu.", e)
                    return@addSnapshotListener
                }

                val comments = snapshots?.documents?.map { doc ->
                    Comment(
                        bookTitle = doc.getString("bookTitle") ?: "",
                        userComment = doc.getString("userComment") ?: "",
                        bookImageUrl = doc.getString("bookImageUrl") ?: ""
                    )
                } ?: emptyList()

                _commentsList.value = comments
            }


    }


    fun addBookCurrentlyList(book: VolumeInfo,onComplete: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val currentlyBook = hashMapOf(
            "title" to book.title,
            "authors" to book.authors,
            "thumbnail" to book.imageLinks?.thumbnail
        )


        db.collection("users").document(userId).collection("currentlyList")
            .document(book.title)
            .set(currentlyBook)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding book to currentlylist",e)
                onComplete(false)
            }
    }



    fun fetchCurrentlyList() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        firestore.collection("users").document(userId).collection("currentlyList")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("ProfileViewModel", "Error fetching currentlyList", e)
                    return@addSnapshotListener
                }

                val currentlyList = snapshots?.documents?.map { doc ->
                    CurrentlyBookList(
                        bookTitle = doc.getString("title") ?: "",
                        bookAuthors = doc.get("authors") as? List<String>,
                        bookImageUrl = doc.getString("thumbnail")
                    )
                } ?: emptyList()

                _currentlyList.value = currentlyList
            }
    }






}
