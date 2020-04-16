package com.example.communicationfile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.addedit_fragment.*
import java.text.SimpleDateFormat
import java.util.*

/**
 *  modify version of code created by timbuchalka for Android Oreo with Kotlin course from www.learnprogramming.academy on Udemy
 *
 */

private const val ARG_NOTES = "note"

private const val TAG = "AddEditFragment"

class AddEditFragment : Fragment() {

    private var note: Note? = null
    private var listener: OnSaveClicked? = null
    private val viewModel by lazy { ViewModelProviders.of(activity!!).get(ComViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note = arguments?.getParcelable(ARG_NOTES)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.addedit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val note = note
        if (note != null){
            addedit_spinner_department.setSelection(when(note.hub){
                getString(R.string.dep2) -> 0
                getString(R.string.dep3) -> 1
                getString(R.string.dep4) -> 2
                getString(R.string.dep5) -> 3
                getString(R.string.dep6) -> 4
                getString(R.string.dep7) -> 5
                else -> throw IllegalArgumentException("wrong parameter note.hub ${note.hub}")
            })
            addedit_market.setText(note.market)
            addedit_month_apply.setText(note.monthApply)
            addedit_package.setText(note.packageNumber)
            addedit_spinner_priority.setSelection(when(note.priority){
                getString(R.string.normal) -> 0
                getString(R.string.urgent) -> 1
                getString(R.string.critical) -> 2
                else -> throw IllegalArgumentException("wrong parameter note.priority ${note.priority}")
            })
            addedit_spinner_status_cc.setSelection(when(note.statusCC){
                getString(R.string.NEW) -> 0
                getString(R.string.closed) -> 1
                getString(R.string.pending) -> 2
                getString(R.string.complaint) -> 3
                else -> throw IllegalArgumentException("wrong parameter note.commentCC ${note.commentCC}")
            })
            addedit_tl.setText(note.tl)
            addedit_spinner_status_inv.setSelection(when(note.statusINV){
                getString(R.string.NEW) -> 0
                getString(R.string.closed) -> 1
                getString(R.string.reopen) -> 2
                else -> throw IllegalArgumentException("wrong parameter note.statusINV ${note.statusINV}")
            })
            addedit_person_reporting.setText(note.personReporting)
            addedit_problem.setText(note.problem)
            addedit_problem_couse.setText(note.problemCouse)
            addedit_comment_cc.setText(note.commentCC)
            addedit_comment_inv.setText(note.commentINV)
        }
    }

    private fun noteFromUi(): Note{
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val formatedDate: String = formatter.format(currentDate)

        val newNote = Note(
            hub = addedit_spinner_department.selectedItem.toString(),
            market = addedit_market.text.toString(),
            monthApply =  addedit_month_apply.text.toString(),
            packageNumber =  addedit_package.text.toString(),
            problem =  addedit_problem.text.toString(),
            personReporting =  addedit_person_reporting.text.toString(),
            priority = addedit_spinner_priority.selectedItem.toString(),
            statusCC = addedit_spinner_status_cc.selectedItem.toString(),
            commentCC = addedit_comment_cc.text.toString(),
            statusINV = addedit_spinner_status_inv.selectedItem.toString(),
            commentINV = addedit_comment_inv.text.toString(),
            problemCouse = addedit_problem_couse.text.toString(),
            tl = addedit_tl.text.toString(),
            date = note?.date ?: formatedDate)
        newNote.id = note?.id ?: 0

        return newNote
    }

    private fun saveNote(){
        val newNote = noteFromUi()
        if (newNote != note){
            Log.d(TAG, "saveTask saving task, id is: ${newNote.id}")
            note = viewModel.saveNote(newNote)
            Log.d(TAG, "saveTask id is: ${newNote.id}")
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (listener is AppCompatActivity) {
            val actionBar = (listener as AppCompatActivity?)?.supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }

        addedit_save_button.setOnClickListener {
            saveNote()
            listener?.onSavedClicked()
        }
        addedit_cancel_button.setOnClickListener {
            listener?.onSavedClicked()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSaveClicked) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnSaveClicked")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnSaveClicked {
        fun onSavedClicked()
    }

    companion object {

        @JvmStatic
        fun newInstance(note: Note?) =
            AddEditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_NOTES, note)
                }
            }
    }
}
