package com.example.tasklist.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.tasklist.data.Task


//Se crea la clase para realizar la conexión con la base de datos. Los métodos *onCreate y *onUpgrade son necesarios.
class DatabaseManager (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        companion object {
            // If you change the database schema, you must increment the database version.
            const val DATABASE_VERSION = 1
            const val DATABASE_NAME = "TaskList.db"
        }

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(Task.SQL_CREATE_TABLE)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(Task.SQL_DROP_TABLE)
            onCreate(db)
        }
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }
}