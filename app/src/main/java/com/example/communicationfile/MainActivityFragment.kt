package com.example.communicationfile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*

/**
 *  modify version of code created by timbuchalka for Android Oreo with Kotlin course from www.learnprogramming.academy on Udemy
 *
 */

private const val TAG = "MainActivityFragment"

class MainActivityFragment: Fragment(), CursorRecyclerViewAdapter.OnNoteClicked{

    interface OnNoteEdit{
        fun onNoteEdit(note: Note)
    }

    private val viewModel by lazy { ViewModelProviders.of(activity!!).get(ComViewModel::class.java)}
    private val mAdapter = CursorRecyclerViewAdapter(null, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate starts")
        super.onCreate(savedInstanceState)
        viewModel.cursor.observe(this, Observer { cursor -> mAdapter.swapCursor(cursor)?.close() })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView start")
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is OnNoteEdit) throw RuntimeException("${context.toString()} must implement OnNoteEdit")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated started")
        super.onViewCreated(view, savedInstanceState)

        var depToShow: String

        with(getDefaultSharedPreferences(activity)){
            depToShow = getString(DEP_TO_SHOW, getString(R.string.dep1))
        }

        notes_list.layoutManager = LinearLayoutManager(context)
        notes_list.adapter = mAdapter
        department.text = depToShow

    }

    override fun onNoteLongClick(note: Note) {
        (activity as OnNoteEdit?)?.onNoteEdit(note)
    }
}