package com.example.talkademy_phase8.util

import android.content.Context
import android.widget.Toast

class UiUtil {
    companion object{
        fun showToast(context: Context,text:String){
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}