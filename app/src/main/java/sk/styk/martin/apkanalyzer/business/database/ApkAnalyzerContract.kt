package sk.styk.martin.apkanalyzer.business.database

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

/**
 * @author Martin Styk
 * @version 06.11.2017.
 */

object ApkAnalyzerContract {

    const val CONTENT_AUTHORITY = "sk.styk.martin.apkanalyzer"
    private val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)

    const val PATH_SEND_DATA = "senddata"

    val ALL_COLUMNS_SEND_DATA = arrayOf(SendDataEntry._ID, SendDataEntry.COLUMN_PACKAGE_NAME,
            SendDataEntry.COLUMN_VERSION, SendDataEntry.COLUMN_TIMESTAMP)

    /**
     * Inner class that defines constant values for the send data database table.
     * Each Entry in the table represents a single Send Data record.
     */
    class SendDataEntry : BaseColumns {
        companion object {

            val CONTENT_URI: Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SEND_DATA)
            const val TABLE_NAME = "senddata"

            const val _ID = BaseColumns._ID
            const val COLUMN_PACKAGE_NAME = "packageName"
            const val COLUMN_VERSION = "packageVersion"
            const val COLUMN_TIMESTAMP = "timestamp"

            const val CONTENT_LIST_TYPE = (ContentResolver.CURSOR_DIR_BASE_TYPE
                    + "/" + CONTENT_AUTHORITY + "/" + PATH_SEND_DATA)
            const val CONTENT_ITEM_TYPE = (ContentResolver.CURSOR_ITEM_BASE_TYPE
                    + "/" + CONTENT_AUTHORITY + "/" + PATH_SEND_DATA)
        }
    }
}
