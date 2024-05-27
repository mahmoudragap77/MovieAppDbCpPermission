package com.training.movieappdbcppermission

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MovieDataBase(context: Context) :
    SQLiteOpenHelper(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val sql ="CREATE TABLE ${Constant.TABLE_NAME} (" +
                "${Constant.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Constant.TITLE} TEXT," +
                "${Constant.YEAR} TEXT," +
                "${Constant.GENRE} TEXT," +
                "${Constant.RATING} FLOAT)"


        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${Constant.TABLE_NAME}")
        onCreate(db)
    }

}