package com.example.communicationfile

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*


/**
 *  modify version of code created by timbuchalka for Android Oreo with Kotlin course from www.learnprogramming.academy on Udemy
 *
 */

private const val TAG = "MainActivity"
const val DEP_TO_SHOW = "depToShow"

class MainActivity : AppCompatActivity(),
                    AddEditFragment.OnSaveClicked,
                    MainActivityFragment.OnNoteEdit{

    private var defaultDepToShow: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        hideEditPane()

    }

    private fun showEditPane(){
        notes_details_container.visibility = View.VISIBLE
        mainFragment.view?.visibility = View.GONE
    }

    private fun hideEditPane(){
        notes_details_container.visibility = View.GONE
        mainFragment.view?.visibility = View.VISIBLE
    }

    private fun removeEditPane(fragment: Fragment? = null){
        Log.d(TAG, "removeEditPane called")
        if (fragment != null){
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }

        hideEditPane()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onSavedClicked() {
        Log.d(TAG, "onSavedClicked called")
        val fragment = supportFragmentManager.findFragmentById(R.id.notes_details_container)
        removeEditPane(fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_add_note -> noteEditRequest(null)
            android.R.id.home -> {
                Log.d(TAG, "onOptionsItemSelected home button pressed")
                val fragment = supportFragmentManager.findFragmentById(R.id.notes_details_container)
                removeEditPane(fragment)
            }
            R.id.menu_dep1 -> {
                defaultDepToShow = getString(R.string.dep1)
                depSelect(defaultDepToShow)
            }
            R.id.menu_dep2 -> {
                defaultDepToShow = getString(R.string.dep2)
                depSelect(defaultDepToShow)
            }
            R.id.menu_dep3 -> {
                defaultDepToShow = getString(R.string.dep3)
                depSelect(defaultDepToShow)
            }
            R.id.menu_dep4 -> {
                defaultDepToShow = getString(R.string.dep4)
                depSelect(defaultDepToShow)
            }
            R.id.menu_dep5 -> {
                defaultDepToShow = getString(R.string.dep5)
                depSelect(defaultDepToShow)
            }
            R.id.menu_dep6 -> {
                defaultDepToShow = getString(R.string.dep6)
                depSelect(defaultDepToShow)
            }
            R.id.menu_dep7 -> {
                defaultDepToShow = getString(R.string.dep7)
                depSelect(defaultDepToShow)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun depSelect(defaultDepToShow: String){
        with(getDefaultSharedPreferences(this).edit()){
                putString(DEP_TO_SHOW, defaultDepToShow)
            apply()
        }
        this.contentResolver?.notifyChange(NotesContracts.CONTENT_URI, null)
        department.text = defaultDepToShow
        Log.d(TAG, "**************** depSelect defaultDepToShow $defaultDepToShow ******************")
    }

    override fun onNoteEdit(note: Note) {
        noteEditRequest(note)
    }

    private fun noteEditRequest(note: Note?){
        Log.d(TAG, "noteEditRequest start")
        val newFragment = AddEditFragment.newInstance(note)
        supportFragmentManager.beginTransaction()
            .replace(R.id.notes_details_container, newFragment)
            .commit()

        Log.d(TAG, "noteEditRequest ends")

        showEditPane()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.notes_details_container)
        if (fragment == null) {
            super.onBackPressed()
        }else{
            removeEditPane(fragment)
        }
    }


}
