package com.example.talkademy_phase8.data.local.room

import androidx.room.*
import com.example.talkademy_phase8.data.entity.Student
import com.example.talkademy_phase8.util.Gender

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(student: Student)

    @Update
    fun update(student: Student)

    @Delete
    fun delete(student: Student)

    @Query("SELECT * FROM students")
    fun getAllStudents(): List<Student>

    @Query("SELECT * FROM students WHERE gender = :gender")
    fun getStudentsByGender(gender: Gender):List<Student>

    @Query("SELECT * FROM students ORDER BY score DESC")
    fun getStudentsSortByScore():List<Student>

    @Query("SELECT * FROM students ORDER BY name ASC")
    fun getStudentsSortByName():List<Student>

    @Query("SELECT * FROM students ORDER BY family ASC")
    fun getStudentsSortByFamily():List<Student>

}