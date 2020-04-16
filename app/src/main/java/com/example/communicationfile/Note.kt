package com.example.communicationfile

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(val hub: String,
           val market: String,
           val monthApply: String,
           val packageNumber: String,
           val problem: String,
           val personReporting: String,
           val priority: String,
           val statusCC: String,
           val commentCC: String,
           val statusINV: String,
           val commentINV: String,
           val problemCouse: String,
           val tl: String,
           val date: String,
           var id: Long = 0): Parcelable
