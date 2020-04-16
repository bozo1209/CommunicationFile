package com.example.communicationfile

import android.app.Application
import android.content.ContentValues
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 *  modify version of code created by timbuchalka for Android Oreo with Kotlin course from www.learnprogramming.academy on Udemy
 *
 */

private const val TAG = "ComViewModel"

class ComViewModel(application: Application): AndroidViewModel(application) {

    private val contentObserver = object : ContentObserver(Handler()){
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            Log.d(TAG, "contentObserver.onChange called uri: $uri")

            loadNotes()
        }
    }
    private val databaseCursor = MutableLiveData<Cursor>()
    val cursor: LiveData<Cursor>
    get() = databaseCursor

    init {
        Log.d(TAG, "ComViewModel created")
        getApplication<Application>().contentResolver.registerContentObserver(NotesContracts.CONTENT_URI, true, contentObserver)
        loadNotes()
    }


    private fun loadNotes(){

        val projection = arrayOf(NotesContracts.Columns.ID,
            NotesContracts.Columns.HUB,
            NotesContracts.Columns.MARKET,
            NotesContracts.Columns.DATE,
            NotesContracts.Columns.MONTH_APPLY,
            NotesContracts.Columns.PACKAGE,
            NotesContracts.Columns.PRIORITY,
            NotesContracts.Columns.PROBLEM,
            NotesContracts.Columns.PERSON_REPORTING,
            NotesContracts.Columns.STATUS_CC,
            NotesContracts.Columns.COMMENT_CC,
            NotesContracts.Columns.STATUS_INV,
            NotesContracts.Columns.COMMENT_INV,
            NotesContracts.Columns.PROBLEM_CAUSE,
            NotesContracts.Columns.TL
            )


        var depToShow: String

        with(getDefaultSharedPreferences(getApplication())){
            depToShow = getString(DEP_TO_SHOW, getApplication<Application>().getString(R.string.dep1))
            Log.d(TAG, "*********** depToShow $depToShow ************")
        }

        val selection = when {
            depToShow != getApplication<Application>().getString(R.string.dep1) -> "${NotesContracts.Columns.HUB} = ? and ${NotesContracts.Columns.STATUS_INV} <> ?"
            depToShow == getApplication<Application>().getString(R.string.dep1) -> "${NotesContracts.Columns.STATUS_INV} <> ?"
            else -> null
        }
        val selectionArgs = when(depToShow){
            getApplication<Application>().getString(R.string.dep1) -> arrayOf(getApplication<Application>().getString(R.string.closed))
            getApplication<Application>().getString(R.string.dep2) -> arrayOf(getApplication<Application>().getString(R.string.dep2), getApplication<Application>().getString(R.string.closed))
            getApplication<Application>().getString(R.string.dep3) -> arrayOf(getApplication<Application>().getString(R.string.dep3), getApplication<Application>().getString(R.string.closed))
            getApplication<Application>().getString(R.string.dep4) -> arrayOf(getApplication<Application>().getString(R.string.dep4), getApplication<Application>().getString(R.string.closed))
            getApplication<Application>().getString(R.string.dep5) -> arrayOf(getApplication<Application>().getString(R.string.dep5), getApplication<Application>().getString(R.string.closed))
            getApplication<Application>().getString(R.string.dep6) -> arrayOf(getApplication<Application>().getString(R.string.dep6), getApplication<Application>().getString(R.string.closed))
            getApplication<Application>().getString(R.string.dep7) -> arrayOf(getApplication<Application>().getString(R.string.dep7), getApplication<Application>().getString(R.string.closed))
            else -> null
        }

        GlobalScope.launch {
            val cursor = getApplication<Application>().contentResolver.query(
                NotesContracts.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )

            databaseCursor.postValue(cursor)
        }
    }

    fun saveNote(note: Note): Note{
        val values = ContentValues()

        if (note.hub.isNotEmpty() && note.packageNumber.isNotEmpty()) {
            values.put(NotesContracts.Columns.HUB, note.hub)
            values.put(NotesContracts.Columns.MARKET, note.market)
            values.put(NotesContracts.Columns.DATE, note.date)
            values.put(NotesContracts.Columns.MONTH_APPLY, note.monthApply)
            values.put(NotesContracts.Columns.PACKAGE, note.packageNumber)
            values.put(NotesContracts.Columns.PRIORITY, note.priority)
            values.put(NotesContracts.Columns.PROBLEM, note.problem)
            values.put(NotesContracts.Columns.PERSON_REPORTING, note.personReporting)
            values.put(NotesContracts.Columns.STATUS_CC, note.statusCC)
            values.put(NotesContracts.Columns.COMMENT_CC, note.commentCC)
            values.put(NotesContracts.Columns.STATUS_INV, note.statusINV)
            values.put(NotesContracts.Columns.COMMENT_INV, note.commentINV)
            values.put(NotesContracts.Columns.PROBLEM_CAUSE, note.problemCouse)
            values.put(NotesContracts.Columns.TL, note.tl)

            if (note.id == 0L) {
                GlobalScope.launch {
                    Log.d(TAG, "saveNote adding new note")
                    val uri = getApplication<Application>().contentResolver?.insert(NotesContracts.CONTENT_URI, values)
                    if (uri != null) {
                        note.id = NotesContracts.getId(uri)
                        Log.d(TAG, "saveNote new id: ${note.id}")
                    }
                }
            } else {
                GlobalScope.launch {
                    Log.d(TAG, "saveNote updating note ${note.id}")
                    getApplication<Application>().contentResolver?.update(NotesContracts.buildUriFromId(note.id), values, null, null)
                }
            }
        }
        return note
    }


    override fun onCleared() {
        Log.d(TAG, "onCleared called")
        getApplication<Application>().contentResolver.unregisterContentObserver(contentObserver)
    }


}