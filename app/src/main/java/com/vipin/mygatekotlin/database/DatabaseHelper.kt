package com.vipin.mygatekotlin.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.vipin.mygatekotlin.model.DataClass

/**
 * Created by vipin.c on 25/06/2019
 */
class DatabaseHelper private constructor(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DB_NAME, factory, DB_VERSION) {

    companion object {

        private var mInstance: DatabaseHelper? = null
        const val DB_NAME = "mygatedatabase.db"
        const val TABLE_NAME = "DataTable"
        const val ID = "id"
        const val PASSCODE = "passcode"
        const val IMAGE_DATA = "imagedata"
        private const val DB_VERSION = 1

        fun getInstance(context: Context): DatabaseHelper? {
            if (mInstance == null) {
                synchronized(this) {
                    if (mInstance == null) {
                        mInstance = DatabaseHelper(context, null)
                    }
                }
            }
            return mInstance
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val query = ("CREATE TABLE " + TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IMAGE_DATA + " BLOB, "
                + PASSCODE + " INTEGER)")

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }


    fun addData(dataClass: DataClass) {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(PASSCODE, dataClass.randomNum)
        contentValues.put(IMAGE_DATA, dataClass.image)
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues)
    }

    val allData: List<DataClass>
        get() {

            val sqLiteDatabase = this.writableDatabase
            val dataClassList = ArrayList<DataClass>()

            val cursor = sqLiteDatabase.query(TABLE_NAME, null, null, null, null, null, ID + " ASC")
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(ID))
                val randomNo = cursor.getInt(cursor.getColumnIndex(PASSCODE))
                val imageData = cursor.getBlob(cursor.getColumnIndex(IMAGE_DATA))

                dataClassList.add(DataClass(id, randomNo, imageData))
            }

            cursor.close()
            return dataClassList
        }

}