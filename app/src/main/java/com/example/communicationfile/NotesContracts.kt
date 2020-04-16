package com.example.communicationfile

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

object NotesContracts {

    internal const val TABLE_NAME = "Notes"

    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME)

    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"

    object Columns{
        const val ID = BaseColumns._ID
        const val HUB = "hub"
        const val MARKET = "market"
        const val DATE = "date"
        const val MONTH_APPLY = "month_apply"
        const val PACKAGE = "package"
        const val PRIORITY = "priority"
        const val PROBLEM = "problem"
        const val PERSON_REPORTING = "person_reporting"
        const val STATUS_CC = "status_cc"
        const val COMMENT_CC = "comment_cc"
        const val STATUS_INV = "status_inv"
        const val COMMENT_INV = "comment_inv"
        const val PROBLEM_CAUSE = "problem_cause"
        const val TL = "tl"
    }

    fun getId(uri: Uri): Long{
        return ContentUris.parseId(uri)
    }

    fun buildUriFromId(id: Long): Uri{
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }
}