package com.example.talkademy_phase8.data.entity

import android.os.Parcelable
//import androidx.room.Entity
//import androidx.room.PrimaryKey
import com.example.talkademy_phase8.util.Gender
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import kotlinx.parcelize.Parcelize

//@Entity(tableName = "students")
@Parcelize
@RealmClass
data class Student(
    val name: String,
    val family: String,
    //@PrimaryKey(autoGenerate = false)
    @PrimaryKey
    val nationalCode: String,
    val score: String,
    val gender: Gender
) : RealmModel,Parcelable {
    override fun toString(): String {
        return "$name $family"
    }
}

