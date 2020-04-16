package com.example.communicationfile

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val TAG = "AppDatabase"

private const val DATABASE_NAME = "CommunicationFile.db"
private const val DATABASE_VERSION = 1

internal class AppDatabase private constructor(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    init {
        Log.d(TAG, "**********************")
    }
    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "onCreate starts")
        val sSQL = """create table ${NotesContracts.TABLE_NAME} (
            ${NotesContracts.Columns.ID} integer primary key not null,
            ${NotesContracts.Columns.HUB} text not null,
            ${NotesContracts.Columns.MARKET} text,
            ${NotesContracts.Columns.DATE} text,
            ${NotesContracts.Columns.MONTH_APPLY} text,
            ${NotesContracts.Columns.PACKAGE} text not null,
            ${NotesContracts.Columns.PRIORITY} text,
            ${NotesContracts.Columns.PROBLEM} text,
            ${NotesContracts.Columns.PERSON_REPORTING} text,
            ${NotesContracts.Columns.STATUS_CC} text,
            ${NotesContracts.Columns.COMMENT_CC} text,
            ${NotesContracts.Columns.STATUS_INV} text,
            ${NotesContracts.Columns.COMMENT_INV} text,
            ${NotesContracts.Columns.PROBLEM_CAUSE} text,
            ${NotesContracts.Columns.TL} text);
        """.replaceIndent(" ")

        Log.d(TAG, sSQL)
        db.execSQL(sSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade starts")
        when(oldVersion){
            1 -> {

            }
            else -> throw IllegalArgumentException("onUpgrade with unknown new version $newVersion")
        }
    }


    companion object : SingletonHolder<AppDatabase, Context>(::AppDatabase)
}