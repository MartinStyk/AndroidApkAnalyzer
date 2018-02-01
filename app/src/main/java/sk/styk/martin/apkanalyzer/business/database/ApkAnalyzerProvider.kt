package sk.styk.martin.apkanalyzer.business.database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import sk.styk.martin.apkanalyzer.business.database.ApkAnalyzerContract.SendDataEntry

/**
 * Database content provider
 *
 * @author Martin Styk
 * @version 07.11.2017.
 */

class ApkAnalyzerProvider : ContentProvider() {

    private lateinit var mDbHelper: DatabaseHelper

    override fun onCreate(): Boolean {
        mDbHelper = DatabaseHelper(context)
        return false
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        val database = mDbHelper.readableDatabase

        val match = uriMatcher.match(uri)

        val cursor: Cursor = when (match) {

            SEND_DATA -> database.query(SendDataEntry.TABLE_NAME,
                    projection, selection, selectionArgs, null, null, sortOrder)

            SEND_DATA_ID -> {
                val selectionSingle = SendDataEntry._ID + "=?"
                val selectionArgsSingle = arrayOf(ContentUris.parseId(uri).toString())
                database.query(SendDataEntry.TABLE_NAME,
                        projection, selectionSingle, selectionArgsSingle, null, null, sortOrder)
            }

            else -> (throw IllegalArgumentException("Cannot query unknown URI " + uri))
        }
        cursor.setNotificationUri(context.contentResolver, uri)

        return cursor
    }

    override fun getType(uri: Uri): String? {
        val match = uriMatcher.match(uri)

        return when (match) {
            SEND_DATA -> SendDataEntry.CONTENT_LIST_TYPE
            SEND_DATA_ID -> SendDataEntry.CONTENT_ITEM_TYPE
            else -> throw IllegalStateException("Unknown URI $uri with match $match")
        }
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val match = uriMatcher.match(uri)

        return when (match) {
            SEND_DATA -> validateAndInsert(uri, contentValues)
            else -> throw IllegalArgumentException("Insertion is not supported for " + uri)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val match = uriMatcher.match(uri)

        val db = mDbHelper.writableDatabase

        return when (match) {
            SEND_DATA_ID -> {
                val recordId = arrayOf(ContentUris.parseId(uri).toString())
                db.delete(SendDataEntry.TABLE_NAME, SendDataEntry._ID + "=?", recordId)
            }
            else -> throw IllegalArgumentException("Delete is not supported for " + uri)
        }
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val match = uriMatcher.match(uri)

        return when (match) {
            SEND_DATA_ID -> validateAndUpdate(uri, contentValues, ContentUris.parseId(uri))
            else -> throw IllegalArgumentException("Update is not supported for " + uri)
        }
    }

    private fun validateAndUpdate(uri: Uri, contentValues: ContentValues?, id: Long): Int {

        validate(contentValues)

        val selection = SendDataEntry._ID + "=?"
        val idArgument = arrayOf(id.toString())

        val result = mDbHelper.writableDatabase.use {
            it.update(SendDataEntry.TABLE_NAME, contentValues, selection, idArgument)
        }
        context.contentResolver.notifyChange(uri, null)

        return result
    }


    private fun validateAndInsert(uri: Uri, contentValues: ContentValues?): Uri? {

        validate(contentValues)

        val id = mDbHelper.writableDatabase.use {
            it.insert(SendDataEntry.TABLE_NAME, null, contentValues)
        }

        if (id.equals(-1)) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri)
            return null
        }

        context.contentResolver.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, id)
    }

    private fun validate(contentValues: ContentValues?) {
        if (contentValues?.getAsString(SendDataEntry.COLUMN_PACKAGE_NAME).isNullOrBlank()) {
            throw IllegalArgumentException("Package name is empty.")
        }
        if (contentValues?.containsKey(SendDataEntry.COLUMN_VERSION) != true) {
            throw IllegalArgumentException("Version is empty.")
        }
    }

    companion object {

        const val LOG_TAG: String = "ApkAnalyzerProvider"

        private const val SEND_DATA = 300
        private const val SEND_DATA_ID = 301
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(ApkAnalyzerContract.CONTENT_AUTHORITY, ApkAnalyzerContract.PATH_SEND_DATA, SEND_DATA)
            uriMatcher.addURI(ApkAnalyzerContract.CONTENT_AUTHORITY, ApkAnalyzerContract.PATH_SEND_DATA + "/#", SEND_DATA_ID)
        }
    }

}
