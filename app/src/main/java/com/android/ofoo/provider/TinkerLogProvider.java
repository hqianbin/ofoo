package com.android.ofoo.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by hp on 2017/2/10.
 * app和tinkerPatchService在不同的进程，此contentprovider为了记录load patch和合成全量dex过程中的日志，方便上传至服务器
 */

public class TinkerLogProvider extends ContentProvider {
    private static final String DB_NAME = "PatchInfo.db";
    private static final String DB_TABLE = "PatchInfoTable";
    private static final int DB_VERSION = 2;

    private static final String DB_CREATE = "create table " + DB_TABLE +
            " (" + UserPatchInfo.ID + " integer primary key autoincrement, " +
            UserPatchInfo.USER_ID + " text, " +
            UserPatchInfo.NAME + " text, " +
            UserPatchInfo.DEVICE_NO + " text not null, " +
            UserPatchInfo.APP_NAME + " text not null, " +
            UserPatchInfo.APP_SYSTEM_NAME + " text not null, " +
            UserPatchInfo.CHANNEL + " text not null, " +
            UserPatchInfo.VERSION_RELEASE + " text not null, " +
            UserPatchInfo.MODEL_PRODUCT + " text not null, " +
            UserPatchInfo.APP_VERSION + " text not null, " +
            UserPatchInfo.PATCH_VERSION + " text not null, " +
            UserPatchInfo.UPDATE_PATCH_VERSION + " text not null, " +
            UserPatchInfo.PATCH_CODE + " text, " +
            UserPatchInfo.PATCH_DESC + " text, " +
            UserPatchInfo.CREATE_TIME + " text not null, " +
            UserPatchInfo.UPDATE_TIME + " text not null);";

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(UserPatchInfo.AUTHORITY, "item", UserPatchInfo.ITEM);
        uriMatcher.addURI(UserPatchInfo.AUTHORITY, "item/#", UserPatchInfo.ITEM_ID);
        uriMatcher.addURI(UserPatchInfo.AUTHORITY, "pos/#", UserPatchInfo.ITEM_POS);
    }

    private static final HashMap<String, String> patchProjectionMap;
    static {
        patchProjectionMap = new HashMap<String, String>();
        patchProjectionMap.put(UserPatchInfo.ID, UserPatchInfo.ID);
        patchProjectionMap.put(UserPatchInfo.USER_ID, UserPatchInfo.USER_ID);
        patchProjectionMap.put(UserPatchInfo.NAME, UserPatchInfo.NAME);
        patchProjectionMap.put(UserPatchInfo.DEVICE_NO, UserPatchInfo.DEVICE_NO);
        patchProjectionMap.put(UserPatchInfo.APP_NAME, UserPatchInfo.APP_NAME);
        patchProjectionMap.put(UserPatchInfo.APP_SYSTEM_NAME, UserPatchInfo.APP_SYSTEM_NAME);
        patchProjectionMap.put(UserPatchInfo.CHANNEL, UserPatchInfo.CHANNEL);
        patchProjectionMap.put(UserPatchInfo.VERSION_RELEASE, UserPatchInfo.VERSION_RELEASE);
        patchProjectionMap.put(UserPatchInfo.MODEL_PRODUCT, UserPatchInfo.MODEL_PRODUCT);
        patchProjectionMap.put(UserPatchInfo.APP_VERSION, UserPatchInfo.APP_VERSION);
        patchProjectionMap.put(UserPatchInfo.PATCH_VERSION, UserPatchInfo.PATCH_VERSION);
        patchProjectionMap.put(UserPatchInfo.UPDATE_PATCH_VERSION, UserPatchInfo.UPDATE_PATCH_VERSION);
        patchProjectionMap.put(UserPatchInfo.PATCH_CODE, UserPatchInfo.PATCH_CODE);
        patchProjectionMap.put(UserPatchInfo.PATCH_DESC, UserPatchInfo.PATCH_DESC);
        patchProjectionMap.put(UserPatchInfo.CREATE_TIME, UserPatchInfo.CREATE_TIME);
        patchProjectionMap.put(UserPatchInfo.UPDATE_TIME, UserPatchInfo.UPDATE_TIME);
    }

    private DBHelper dbHelper = null;
    private ContentResolver resolver = null;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        resolver = context.getContentResolver();
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);

        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case UserPatchInfo.ITEM:
                return UserPatchInfo.CONTENT_TYPE;
            case UserPatchInfo.ITEM_ID:
            case UserPatchInfo.ITEM_POS:
                return UserPatchInfo.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(uriMatcher.match(uri) != UserPatchInfo.ITEM) {
            throw new IllegalArgumentException("Error Uri: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(DB_TABLE, UserPatchInfo.ID, values);
        if(id < 0) {
            throw new SQLiteException("Unable to insert " + values + " for " + uri);
        }

        Uri newUri = ContentUris.withAppendedId(uri, id);
        resolver.notifyChange(newUri, null);

        return newUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;

        switch(uriMatcher.match(uri)) {
            case UserPatchInfo.ITEM: {
                count = db.update(DB_TABLE, values, selection, selectionArgs);
                break;
            }
            case UserPatchInfo.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.update(DB_TABLE, values, UserPatchInfo.ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        resolver.notifyChange(uri, null);

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;

        switch(uriMatcher.match(uri)) {
            case UserPatchInfo.ITEM: {
                count = db.delete(DB_TABLE, selection, selectionArgs);
                break;
            }
            case UserPatchInfo.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                count = db.delete(DB_TABLE, UserPatchInfo.ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " and (" + selection + ')' : ""), selectionArgs);
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        resolver.notifyChange(uri, null);

        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        String limit = null;

        switch (uriMatcher.match(uri)) {
            case UserPatchInfo.ITEM: {
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(patchProjectionMap);
                break;
            }
            case UserPatchInfo.ITEM_ID: {
                String id = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(patchProjectionMap);
                sqlBuilder.appendWhere(UserPatchInfo.ID + "=" + id);
                break;
            }
            case UserPatchInfo.ITEM_POS: {
                String pos = uri.getPathSegments().get(1);
                sqlBuilder.setTables(DB_TABLE);
                sqlBuilder.setProjectionMap(patchProjectionMap);
                limit = pos + ", 1";
                break;
            }
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }

        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, TextUtils.isEmpty(sortOrder) ? UserPatchInfo.DEFAULT_SORT_ORDER : sortOrder, limit);
        cursor.setNotificationUri(resolver, uri);

        return cursor;
    }

    public Bundle call(String method, String request, Bundle args) {
        if(method.equals(UserPatchInfo.METHOD_GET_ITEM_COUNT)) {
            return getItemCount();
        }
        throw new IllegalArgumentException("Error method call: " + method);
    }

    private Bundle getItemCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + DB_TABLE, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(UserPatchInfo.KEY_ITEM_COUNT, count);

        cursor.close();
        db.close();

        return bundle;
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}
