package com.example.bookclubapp.util

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

fun makeToast(message: String, context: Context){
    Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
}

fun makeAlertDialog(context: Context) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    //alertDialogBuilder.setTitle("How To Use")
    //alertDialogBuilder.setMessage(R.string.how_to_use)

    alertDialogBuilder.create()
    alertDialogBuilder.show()
}