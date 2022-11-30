package com.example.mazegame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mazegame.database.contracts.LevelsContract;

public class LevelsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Levels.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LevelsContract.LevelsEntry.TABLE_NAME + " (" +
                    LevelsContract.LevelsEntry._ID + " INTEGER PRIMARY KEY," +
                    LevelsContract.LevelsEntry.COLUMN_NAME_ROWS + " INT," +
                    LevelsContract.LevelsEntry.COLUMN_NAME_COLS + " INT," +
                    LevelsContract.LevelsEntry.COLUMN_NAME_SCORE + " INT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LevelsContract.LevelsEntry.TABLE_NAME;
    private static final String SQL_INSERT_DEFAULT_VALUES =
            "INSERT INTO " + LevelsContract.LevelsEntry.TABLE_NAME + " (" +
                    LevelsContract.LevelsEntry.COLUMN_NAME_ROWS + "," +
                    LevelsContract.LevelsEntry.COLUMN_NAME_COLS + "," +
                    LevelsContract.LevelsEntry.COLUMN_NAME_SCORE + ")" +
            " VALUES (5, 5, 0), (13, 7, 0), (20, 10, 0)";

    public LevelsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_INSERT_DEFAULT_VALUES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }

    public Cursor getLevels() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(LevelsContract.LevelsEntry.TABLE_NAME,
                null, null, null, null, null, null) ;
    }

    public void updateScore(int newScore, int id) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(LevelsContract.LevelsEntry.COLUMN_NAME_SCORE, newScore);
        String selection = LevelsContract.LevelsEntry._ID + " == ?";
        String[] selectionArgs = {String.valueOf(id)};
        db.update(LevelsContract.LevelsEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public Cursor getLevel(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = LevelsContract.LevelsEntry._ID + " == ?";
        String[] selectionArgs = {String.valueOf(id)};
        return db.query(LevelsContract.LevelsEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }
}
