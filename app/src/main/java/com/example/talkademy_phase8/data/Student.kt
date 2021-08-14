package com.example.talkademy_phase8.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.talkademy_phase8.util.Gender
import kotlinx.parcelize.Parcelize

@Entity(tableName = "students")
@Parcelize
data class Student(
    val name:String,
    val family:String,
    @PrimaryKey(autoGenerate = false)
    val nationalCode :String,
    val score:String,
    val gender: Gender
): Parcelable

