package com.example.indengdictionary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DictionaryDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public DictionaryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addWord(String wordIndonesia, String wordEnglish) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_WORD_INDONESIA, wordIndonesia);
        values.put(DatabaseHelper.COLUMN_WORD_ENGLISH, wordEnglish);
        database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    public Cursor getAllWords() {
        return database.query(DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_WORD_INDONESIA, DatabaseHelper.COLUMN_WORD_ENGLISH},
                null, null, null, null, null);
    }

    public Cursor getWord(String wordIndonesia) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_WORD_INDONESIA, DatabaseHelper.COLUMN_WORD_ENGLISH},
                DatabaseHelper.COLUMN_WORD_INDONESIA + "=?",
                new String[]{wordIndonesia}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void updateWord(long id, String wordIndonesia, String wordEnglish) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_WORD_INDONESIA, wordIndonesia);
        values.put(DatabaseHelper.COLUMN_WORD_ENGLISH, wordEnglish);
        database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteWord(long id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}