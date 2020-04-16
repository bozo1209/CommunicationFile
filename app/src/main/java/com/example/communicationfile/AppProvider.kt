package com.example.communicationfile

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log


/**
 *  modify version of code created by timbuchalka for Android Oreo with Kotlin course from www.learnprogramming.academy on Udemy
 *
 */

private const val TAG = "AppProvider"

const val CONTENT_AUTHORITY = "com.example.communicationfile.provider"

private const val NOTES = 100
private const val NOTES_ID = 101

val CONTENT_AUTHORITY_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

class AppProvider: ContentProvider() {


    private val uriMatcher by lazy { buildUriMatcher() }

    private fun buildUriMatcher(): UriMatcher{
        Log.d(TAG, "buildUriMatcher starts")

        val matcher = UriMatcher(UriMatcher.NO_MATCH)

        matcher.addURI(CONTENT_AUTHORITY, NotesContracts.TABLE_NAME, NOTES)
        matcher.addURI(CONTENT_AUTHORITY, "${NotesContracts.TABLE_NAME}/#", NOTES_ID)

        return matcher
    }

    override fun insert(uri: Uri, values: ContentValues): Uri {
        Log.d(TAG, "insert called uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "insert match is $match")

        val recordId: Long
        val returnUri: Uri

        when(match){
            NOTES -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                recordId = db.insert(NotesContracts.TABLE_NAME, null, values)
                if(recordId != -1L){
                    returnUri = NotesContracts.buildUriFromId(recordId)
                }else{
                    throw SQLException("failed to insert. Uri was $uri")
                }
            }
            else -> throw IllegalArgumentException("Unknown uri: $uri")
        }

        if (recordId > 0){
            Log.d(TAG, "insert notifyChange with $uri")
            context?.contentResolver?.notifyChange(uri, null)
        }

        Log.d(TAG, "Exiting insert, returning $returnUri")
        return returnUri
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        Log.d(TAG, "query called uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "query match is $match")

        val queryBuilder = SQLiteQueryBuilder()

        when(match){
            NOTES -> queryBuilder.tables = NotesContracts.TABLE_NAME
            NOTES_ID -> {
                queryBuilder.tables = NotesContracts.TABLE_NAME
                val notesId = NotesContracts.getId(uri)
                queryBuilder.appendWhere("${NotesContracts.Columns.ID} = ")
                queryBuilder.appendWhereEscapeString("$notesId")
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        val db = AppDatabase.getInstance(context).readableDatabase

        val cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        Log.d(TAG, "query: rows in returned cursor = ${cursor.count}")
        Log.d(TAG, "query: returned cursor = $cursor")
        Log.d(TAG, "uri = $uri")

        return cursor
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate starts")
        return true
    }

    override fun update(uri: Uri, values: ContentValues, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "update called uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "update match is $match")

        val count: Int
        var selectionCriteria: String

        when(match){
            NOTES -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.update(NotesContracts.TABLE_NAME, values, selection, selectionArgs)
            }
            NOTES_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = NotesContracts.getId(uri)
                selectionCriteria = "${NotesContracts.Columns.ID} = $id"

                if(selection != null && selection.isNotEmpty()){
                    selectionCriteria += "and ($selection)"
                }

                count = db.update(NotesContracts.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }
            else -> throw IllegalArgumentException("unknown uri $uri")
        }
        if(count > 0){
            Log.d(TAG, "update settings notifyChange with uri $uri")
            context?.contentResolver?.notifyChange(uri, null)
        }

        Log.d(TAG, "exiting update, returning $count")
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "update called uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "update match is $match")

        val count: Int
        var selectionCriteria: String

        when(match){
            NOTES -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                count = db.delete(NotesContracts.TABLE_NAME, selection, selectionArgs)
            }
            NOTES_ID -> {
                val db = AppDatabase.getInstance(context).writableDatabase
                val id = NotesContracts.getId(uri)
                selectionCriteria = "${NotesContracts.Columns.ID} = $id"

                if(selection != null && selection.isNotEmpty()){
                    selectionCriteria += "and ($selection)"
                }

                count = db.delete(NotesContracts.TABLE_NAME, selectionCriteria, selectionArgs)
            }
            else -> throw IllegalArgumentException("unknown uri $uri")
        }
        if(count > 0){
            Log.d(TAG, "update settings notifyChange with uri $uri")
            context?.contentResolver?.notifyChange(uri, null)
        }

        Log.d(TAG, "exiting update, returning $count")
        return count
    }

    override fun getType(uri: Uri): String {
        val match = uriMatcher.match(uri)

        return when(match){
            NOTES -> NotesContracts.CONTENT_TYPE
            NOTES_ID -> NotesContracts.CONTENT_ITEM_TYPE
            else -> throw IllegalArgumentException("unknown Uri: $uri")
        }
    }
}