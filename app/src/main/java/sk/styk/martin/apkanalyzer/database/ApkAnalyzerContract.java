package sk.styk.martin.apkanalyzer.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Martin Styk on 06.11.2017.
 */

public class ApkAnalyzerContract {

    private ApkAnalyzerContract() {
    }

    public static final String CONTENT_AUTHORITY = "sk.styk.martin.apkanalyzer";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SEND_DATA = "senddata";


    public static final String[] ALL_COLUMNS_SEND_DATA = {
            SendDataEntry._ID,
            SendDataEntry.COLUMN_HASH,
            SendDataEntry.COLUMN_PACKAGE_NAME,
            SendDataEntry.COLUMN_VERSION,
            SendDataEntry.COLUMN_TIMESTAMP
    };

    /**
     * Inner class that defines constant values for the send data database table.
     * Each Entry in the table represents a single Send Data record.
     */
    public static final class SendDataEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SEND_DATA);
        public static final String TABLE_NAME = "senddata";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_HASH = "hash";
        public final static String COLUMN_PACKAGE_NAME = "packageName";
        public final static String COLUMN_VERSION = "packageVersion";
        public final static String COLUMN_TIMESTAMP = "timestamp";

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_SEND_DATA;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_SEND_DATA;
    }
}
