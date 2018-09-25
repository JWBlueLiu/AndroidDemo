package com.example.demoCollection.logic;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;
import java.io.File;

/**
 * Created by JWBlue.Liu on 15/12/8.
 */
public class StickerProvider extends ContentProvider {
    public String TAG = getClass().getSimpleName();
    private DBHelper mDBHelper;

    public static final String TABLE_PERSON = "person";

    private static final int PERSON_DIR = 0;
    private static final int PERSON_ITEM = 1;

    private static UriMatcher uriMatcher;
    public static String AUTHORITY = "com.example.demoCollection.StickerProvider";
    public static String PERSON_URI = "content://" + AUTHORITY + File.separator + TABLE_PERSON + File.separator;

    public static String VND = "vnd.";
    public static String CURSOR_DIR = "android.cursor.dir/";
    public static String CURSOR_ITEM = "android.cursor.item/";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_PERSON, PERSON_DIR);
        uriMatcher.addURI(AUTHORITY, TABLE_PERSON + "/#", PERSON_ITEM);
    }

    @Override
    public boolean onCreate() {
        Log.i(TAG, "onCreate");
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    @WorkerThread
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.i(TAG, "query");
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case PERSON_DIR:
                cursor = db.query(TABLE_PERSON, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PERSON_ITEM:
                String personId = uri.getLastPathSegment();
                cursor = db.query(TABLE_PERSON, projection, "_id = ?", new String[]{personId}, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "insert");
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case PERSON_DIR:
            case PERSON_ITEM:
                long newPersonId = db.insert(TABLE_PERSON, null, values);
                uriReturn = Uri.parse(PERSON_URI + newPersonId);
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(TAG, "delete");
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)) {
            case PERSON_DIR:
                deleteRows = db.delete(TABLE_PERSON, selection, selectionArgs);
                break;
            case PERSON_ITEM:
                String personId = uri.getLastPathSegment();
                deleteRows = db.delete(TABLE_PERSON, "_id = ?", new String[]{personId});
                break;
        }
        return deleteRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i(TAG, "update");
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)) {
            case PERSON_DIR:
                updateRows = db.update(TABLE_PERSON, values, selection, selectionArgs);
                break;
            case PERSON_ITEM:
                String personId = uri.getLastPathSegment();
                updateRows = db.update(TABLE_PERSON, values, "_id = ?", new String[]{personId});
                break;
        }
        return updateRows;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.i(TAG, "getType");
        switch (uriMatcher.match(uri)) {
            case PERSON_DIR:
                return VND + CURSOR_DIR + VND + AUTHORITY + TABLE_PERSON;
            case PERSON_ITEM:
                return VND + CURSOR_ITEM + VND + AUTHORITY + TABLE_PERSON;
        }
        return null;
    }
}