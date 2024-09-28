package com.example.bookclubapp.util

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

fun makeToast(message: String, context: Context){
    Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
}

fun makeAlertDialog(context: Context) {
    val alertDialogBuilder = AlertDialog.Builder(context)


    alertDialogBuilder.create()
    alertDialogBuilder.show()
}