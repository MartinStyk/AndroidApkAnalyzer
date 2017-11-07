package sk.styk.martin.apkanalyzer.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import sk.styk.martin.apkanalyzer.database.ApkAnalyzerContract.SendDataEntry;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;

/**
 * Created by Martin Styk on 07.11.2017.
 */

public class ApkAnalyzerProvider extends ContentProvider {

    public static final String LOG_TAG = ApkAnalyzerProvider.class.getSimpleName();

    private static final int SEND_DATA = 300;
    private static final int SEND_DATA_ID = 301;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ApkAnalyzerContract.CONTENT_AUTHORITY, ApkAnalyzerContract.PATH_SEND_DATA, SEND_DATA);
        sUriMatcher.addURI(ApkAnalyzerContract.CONTENT_AUTHORITY, ApkAnalyzerContract.PATH_SEND_DATA + "/#", SEND_DATA_ID);
    }

    private DatabaseHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case SEND_DATA:
                cursor = database.query(SendDataEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case SEND_DATA_ID:
                selection = SendDataEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(SendDataEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SEND_DATA:
                return SendDataEntry.CONTENT_LIST_TYPE;
            case SEND_DATA_ID:
                return SendDataEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SEND_DATA:
                return validateAndInsert(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (match) {
            case SEND_DATA_ID:
                String[] recordId = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(SendDataEntry.TABLE_NAME, SendDataEntry._ID + "=?", recordId);
            default:
                throw new IllegalArgumentException("Delete is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SEND_DATA_ID:
                return validateAndUpdate(uri, contentValues, ContentUris.parseId(uri));
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int validateAndUpdate(final Uri uri, final ContentValues contentValues, long id) {

        validate(contentValues);

        final String selection = SendDataEntry._ID + "=?";
        final String[] idArgument = new String[]{String.valueOf(id)};

        getContext().getContentResolver().notifyChange(uri, null);
        return mDbHelper.getWritableDatabase().update(
                SendDataEntry.TABLE_NAME, contentValues, selection, idArgument);
    }


    private Uri validateAndInsert(Uri uri, ContentValues contentValues) {

        validate(contentValues);

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(SendDataEntry.TABLE_NAME, null, contentValues);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private void validate(ContentValues contentValues) {
        if (!contentValues.containsKey(SendDataEntry.COLUMN_HASH)) {
            throw new IllegalArgumentException("Hash is empty.");
        }
        if (!contentValues.containsKey(SendDataEntry.COLUMN_PACKAGE_NAME) ||
                contentValues.getAsString(SendDataEntry.COLUMN_PACKAGE_NAME) == null ||
                contentValues.getAsString(SendDataEntry.COLUMN_PACKAGE_NAME).trim().isEmpty()) {
            throw new IllegalArgumentException("Package name is empty.");
        }
        if (!contentValues.containsKey(SendDataEntry.COLUMN_VERSION)) {
            throw new IllegalArgumentException("Version is empty.");
        }
    }

}
