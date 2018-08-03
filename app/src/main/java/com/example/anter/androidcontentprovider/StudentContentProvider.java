package com.example.anter.androidcontentprovider;

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

/**
 * AndroidContentProvider
 * <p>
 *
 * @author Anter
 * @date 2018/5/23
 */

public class StudentContentProvider extends ContentProvider {

    private static final String TAG = "[ContentProvider]";

    private static UriMatcher matcher;
    private DatabaseHelper openHelper;
    // The same to AndroidManifest.xml: android:authorities="com.example.anter.student"
    private static final String AUTHORITY = "com.example.anter.student";
    private static final String TABLE_PERSON_NAME = DatabaseHelper.TABLE_NAME;

    private static final int PERSON_INSERT_CODE = 1000;
    private static final int PERSON_DELETE_CODE = 10001;
    private static final int PERSON_UPDATE_CODE = 10002;
    private static final int PERSON_QUERY_ALL_CODE = 10003;
    private static final int PERSON_QUERY_ONE_CODE = 10004;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Uri=content://com.gunther.person/person/insert
        matcher.addURI(AUTHORITY, "student/insert", PERSON_INSERT_CODE);
        matcher.addURI(AUTHORITY, "student/delete", PERSON_DELETE_CODE);
        matcher.addURI(AUTHORITY, "student/update", PERSON_UPDATE_CODE);
        matcher.addURI(AUTHORITY, "student", PERSON_QUERY_ALL_CODE);
        matcher.addURI(AUTHORITY, "student/#", PERSON_QUERY_ONE_CODE);
    }

    @Override
    public boolean onCreate() {
        openHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.e(TAG, "query:  uri = " + uri);
        int matchCode = matcher.match(uri);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        switch (matchCode) {
            case PERSON_QUERY_ALL_CODE:
                return db.query(TABLE_PERSON_NAME, strings, s, strings1, null, null, s1);
            case PERSON_QUERY_ONE_CODE:
                long parseId = ContentUris.parseId(uri);
                return db.query(TABLE_PERSON_NAME, strings, "ID = ?", new String[]{parseId + ""}, null, null, s1);
            default:
                throw new IllegalArgumentException("Uri don't match: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.e(TAG, "insert:  uri = " + uri);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        long id = db.insert(TABLE_PERSON_NAME, null, contentValues);
        db.close();
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.e(TAG, "delete:  uri = " + uri);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        int count = db.delete(TABLE_PERSON_NAME, s, strings);
        db.close();
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.e(TAG, "update:  uri = " + uri);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        int count = db.update(TABLE_PERSON_NAME, contentValues, s, strings);
        db.close();
        return count;
    }

}
