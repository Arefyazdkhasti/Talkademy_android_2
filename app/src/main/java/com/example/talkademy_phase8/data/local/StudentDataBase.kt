package com.example.talkademy_phase8.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.talkademy_phase8.data.Student

@Database(entities = [Student::class],version = 1)
abstract class StudentDataBase :RoomDatabase(){

    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var instance: StudentDataBase? = null

        fun getDatabase(context: Context): StudentDataBase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, StudentDataBase::class.java, "students.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}