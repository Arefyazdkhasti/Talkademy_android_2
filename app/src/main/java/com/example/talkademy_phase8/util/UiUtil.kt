package com.example.talkademy_phase8.util

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.talkademy_phase8.ui.framgent.DATABASE_KEY
import com.example.talkademy_phase8.ui.framgent.PREF_KEY

class UiUtil {
    companion object{
        fun showToast(context: Context,text:String){
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
        fun getPreferredDataBase(activity: AppCompatActivity): String {
            val sharedPref = activity.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            return sharedPref?.getString(DATABASE_KEY, DataBase.Sqlite.name) ?: DataBase.Sqlite.name
        }

    }
}