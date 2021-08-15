package com.example.talkademy_phase8.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.talkademy_phase8.data.Student

class DataBaseOpenHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "StudentDatabase"
        private const val TABLE_NAME = "StudentTable"
        private const val KEY_NAME = "name"
        private const val KEY_FAMILY = "family"
        private const val KEY_NATIONAL_CODE = "nationalCode"
        private const val KEY_SCORE = "score"
        private const val KEY_GENDER = "gender"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_NAME + " TEXT,"
                + KEY_FAMILY + " TEXT,"
                + KEY_NATIONAL_CODE + " TEXT Primary KEY,"
                + KEY_SCORE + " TEXT,"
                + KEY_GENDER + " TEXT" + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    fun addStudent(student: Student): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, student.name)
        contentValues.put(KEY_FAMILY, student.family)
        contentValues.put(KEY_NATIONAL_CODE, student.nationalCode)
        contentValues.put(KEY_SCORE, student.score)
        contentValues.put(KEY_GENDER, student.gender.name)
        var success:Long = 0
        if (!checkStudentExists(student.nationalCode)) {
           success = db.insert(TABLE_NAME, null, contentValues)
        }

        db.close()
        return success
    }

    //method to read data
    fun getAllStudents(): List<Student> {
        val empList = ArrayList<Student>()
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var name: String
        var family: String
        var nationalCode: String
        var score: String
        var gender: String
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                family = cursor.getString(cursor.getColumnIndex(KEY_FAMILY))
                nationalCode = cursor.getString(cursor.getColumnIndex(KEY_NATIONAL_CODE))
                score = cursor.getString(cursor.getColumnIndex(KEY_SCORE))
                gender = cursor.getString(cursor.getColumnIndex(KEY_GENDER))
                val emp = Student(name, family, nationalCode, score, enumValueOf(gender))
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    fun updateStudent(student: Student): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, student.name)
        contentValues.put(KEY_FAMILY, student.family)
        contentValues.put(KEY_NATIONAL_CODE, student.nationalCode)
        contentValues.put(KEY_SCORE, student.score)
        contentValues.put(KEY_GENDER, student.gender.name)
        val success = db.update(
            TABLE_NAME,
            contentValues,
            "$KEY_NATIONAL_CODE= ${student.nationalCode}",
            null
        )
        db.close()
        return success
    }

    fun deleteEmployee(student: Student): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NATIONAL_CODE, student.nationalCode) // EmpModelClass UserId
        val success = db.delete(TABLE_NAME, "$KEY_NATIONAL_CODE= ${student.nationalCode}", null)
        db.close()
        return success
    }

    fun checkStudentExists(nationalCode: String): Boolean {
        val sqLiteDatabase = this.readableDatabase
        val cursor = sqLiteDatabase.rawQuery(
            ("SELECT * FROM "
                    + TABLE_NAME
                    ) + " WHERE "
                    + KEY_NATIONAL_CODE +
                    " = ?", arrayOf(nationalCode)
        )
        return cursor.moveToFirst()
    }

}