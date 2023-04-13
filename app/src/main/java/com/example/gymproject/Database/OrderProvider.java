package com.example.gymproject.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.IllformedLocaleException;

public class OrderProvider extends ContentProvider {

    public static final int ORDER = 100;

    public static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(OrderContract.CONTENT_AUTHORITY, OrderContract.PATH, ORDER);
    }

    public OrderHelper mHelper;


    @Override
    public boolean onCreate() {
        mHelper = new OrderHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {

        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case ORDER:
                cursor = database.query(OrderContract.OrderEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("CANT QUERY");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType( Uri uri) {
        return null;
    }


    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case ORDER:
                return insertCart (uri,values);

            default:
                throw new IllegalArgumentException("Cant insert data");
        }
    }

    private Uri insertCart(Uri uri, ContentValues values) {

        String name = values.getAsString(OrderContract.OrderEntry.COLUMN_NAME);
        if (name == null){
            throw new IllegalArgumentException("Nombre es requerido");
        }

        String quantity = values.getAsString(OrderContract.OrderEntry.COLUMN_QUANTITY);
        if (name == null){
            throw new IllegalArgumentException("Cantidad es requerida");
        }

        String prince = values.getAsString(OrderContract.OrderEntry.COLUMN_PRICE);
        if (name == null){
            throw new IllegalArgumentException("Precio es requerido");
        }

        SQLiteDatabase database = mHelper.getWritableDatabase();
        long id = database.insert(OrderContract.OrderEntry.TABLE_NAME, null, values);

        if (id == -1){
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri,id);

    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {

        int rowsDelete;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match){
            case ORDER:
                rowsDelete = database.delete(OrderContract.OrderEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Cannot delete");
        }

        if (rowsDelete!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsDelete;
    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        return 0;
    }
}
