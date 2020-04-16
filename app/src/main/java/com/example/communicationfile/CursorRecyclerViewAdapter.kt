package com.example.communicationfile

import android.database.Cursor
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


/**
 *  modify version of code created by timbuchalka for Android Oreo with Kotlin course from www.learnprogramming.academy on Udemy
 *
 */

private const val TAG = "CursorRVAdapter"

class NoteViewHolder(val containerView: View): RecyclerView.ViewHolder(containerView){
    var hub: TextView = containerView.findViewById(R.id.notes_list_hub)
    var market: TextView = containerView.findViewById(R.id.notes_list_market)
    var dataAdded: TextView = containerView.findViewById(R.id.notes_list_date)
    var monthApply: TextView = containerView.findViewById(R.id.notes_list_month_apply)
    var packageNum: TextView = containerView.findViewById(R.id.notes_list_package)
    var priority: TextView = containerView.findViewById(R.id.notes_list_priority)
    var problem: TextView = containerView.findViewById(R.id.notes_list_problem)
    var personReporting: TextView = containerView.findViewById(R.id.notes_list_person_reporting)
    var statusCC: TextView = containerView.findViewById(R.id.notes_list_status_cc)
    var commentCC: TextView = containerView.findViewById(R.id.notes_list_comment_cc)
    var statusINV: TextView = containerView.findViewById(R.id.notes_list_status_inv)
    var commentINV: TextView = containerView.findViewById(R.id.notes_list_comment_inv)
    var problemCouse: TextView = containerView.findViewById(R.id.notes_list_problem_cause)
    var tl: TextView = containerView.findViewById(R.id.notes_list_tl)

    fun bind(note: Note, listener: CursorRecyclerViewAdapter.OnNoteClicked){
        hub.text = note.hub
        market.text = note.market
        dataAdded.text = note.date
        monthApply.text = note.monthApply
        packageNum.text = note.packageNumber
        problem.text = note.problem
        priority.text = note.priority
        personReporting.text = note.personReporting
        statusCC.text = note.statusCC
        commentCC.text = note.commentCC
        statusINV.text = note.statusINV
        commentINV.text = note.commentINV
        problemCouse.text = note.problemCouse
        tl.text = note.tl

        hub.visibility = View.VISIBLE
        market.visibility = View.VISIBLE
        dataAdded.visibility = View.VISIBLE
        monthApply.visibility = View.VISIBLE
//        packageNum.visibility = View.VISIBLE
        priority.visibility = View.VISIBLE
        personReporting.visibility = View.VISIBLE
        statusCC.visibility = View.VISIBLE
        commentCC.visibility = View.VISIBLE
        statusINV.visibility = View.VISIBLE
        commentINV.visibility = View.VISIBLE
        problemCouse.visibility = View.VISIBLE
        tl.visibility = View.VISIBLE



        when(priority.text) {
            itemView.context.getString(R.string.critical) -> priority.setBackgroundColor(Color.RED)
            itemView.context.getString(R.string.urgent) -> priority.setBackgroundColor(Color.YELLOW)
            else -> priority.setBackgroundColor(Color.TRANSPARENT)
        }

        when(statusCC.text){
            itemView.context.getString(R.string.closed) -> statusCC.setBackgroundColor(Color.GREEN)
            itemView.context.getString(R.string.pending) -> statusCC.setBackgroundColor(Color.YELLOW)
            else -> statusCC.setBackgroundColor(Color.TRANSPARENT)
        }

        when(statusINV.text){
            itemView.context.getString(R.string.reopen) -> statusINV.setBackgroundColor(Color.RED)
            else -> statusINV.setBackgroundColor(Color.TRANSPARENT)
        }

        containerView.setOnLongClickListener {
            Log.d(TAG, "onLongClick note dep is ${note.hub}")
            Log.d(TAG, "onLongClick note id is ${note.id}")
            listener.onNoteLongClick(note)
            true
        }
    }
}
class CursorRecyclerViewAdapter(private var cursor: Cursor?, private val listener: OnNoteClicked): RecyclerView.Adapter<NoteViewHolder>() {

    interface OnNoteClicked{
        fun onNoteLongClick(note: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        Log.d(TAG, "onCreateViewHolder new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_list_items, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount starts")
        val cursor = cursor
        val count = if(cursor == null || cursor.count == 0) 1 else cursor.count
        Log.d(TAG, "getItemCount count = $count")
        return count
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val cursor = cursor

        if(cursor == null || cursor.count == 0){
            Log.d(TAG, "onBindViewHolder cursor == null || cursor.count == 0")
            holder.problem.setText((R.string.pls_add_record))

            val cos = holder.problem.setText((R.string.pls_add_record))
            Log.d(TAG, "onBindViewHolder cos = $cos")
            holder.hub.visibility = View.GONE
            holder.market.visibility = View.GONE
            holder.dataAdded.visibility = View.GONE
            holder.monthApply.visibility = View.GONE
//            holder.packageNum.visibility = View.GONE
            holder.priority.visibility = View.GONE
            holder.personReporting.visibility = View.GONE
            holder.statusCC.visibility = View.GONE
            holder.commentCC.visibility = View.GONE
            holder.statusINV.visibility = View.GONE
            holder.commentINV.visibility = View.GONE
            holder.problemCouse.visibility = View.GONE
            holder.tl.visibility = View.GONE
        }else{
            Log.d(TAG, "onBindViewHolder ELSE")
            if(!cursor.moveToPosition(position)){
                throw IllegalStateException("Couldn't move cursor to position $position")
            }

            val note = Note(
                hub = cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.HUB)),
                market = cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.MARKET)),
                monthApply =  cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.MONTH_APPLY)),
                packageNumber =  cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.PACKAGE)),
                problem =  cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.PROBLEM)),
                personReporting =  cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.PERSON_REPORTING)),
                priority = cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.PRIORITY)),
                statusCC = cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.STATUS_CC)),
                commentCC = cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.COMMENT_CC)),
                statusINV = cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.STATUS_INV)),
                commentINV = cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.COMMENT_INV)),
                problemCouse = cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.PROBLEM_CAUSE)),
                tl = cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.TL)),
                date = cursor.getString(cursor.getColumnIndex(NotesContracts.Columns.DATE))
            )
            note.id = cursor.getLong(cursor.getColumnIndex(NotesContracts.Columns.ID))


            holder.bind(note, listener)
        }
    }

    fun swapCursor(newCursor: Cursor?): Cursor?{
        Log.d(TAG, "swapCursor ***********************************************")
        if(newCursor === cursor){
            return null
        }
        val numItems = itemCount
        val oldCursor = cursor
        cursor = newCursor
        if (newCursor != null){
            notifyDataSetChanged()
        }else{
            notifyItemRangeRemoved(0, numItems)
        }
        return oldCursor
    }

}