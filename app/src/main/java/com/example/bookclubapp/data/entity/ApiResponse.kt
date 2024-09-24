package com.example.bookclubapp.data.entity

import com.google.gson.annotations.SerializedName

data class ApiResponse(@SerializedName("success") var success:Int,
                       @SerializedName("message") var message:String)
