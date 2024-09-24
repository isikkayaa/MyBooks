package com.example.bookclubapp.data.entity

import java.io.Serializable

data class VolumeInfo(
val title: String,
val authors: List<String>?,
val description: String? = "Description not available", // Varsayılan değer
val imageLinks: ImageLinks?,
) : Serializable

