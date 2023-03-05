package com.example.audionotes.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "audio_notes.db";
    private static final int VERSION_NUMBER = 1;
    public static final String TABLE_NAME = "audio_notes_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE_NOTES = "title";
    public static final String COLUMN_DATE_CREATE_NOTES = "date_create";
    public static final String COLUMN_TIME_CREATE_NOTES = "time_create";
    public static final String COLUMN_FILE_PATH = "file_path";
    public static final String COLUMN_FILE_NAME = "file_name";
    public static final String COLUMN_TIMING_AUDIO = "timing_audio";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE_NOTES + " TEXT NOT NULL, " +
                        COLUMN_DATE_CREATE_NOTES + " TEXT NOT NULL, " +
                        COLUMN_TIME_CREATE_NOTES + " TEXT NOT NULL, " +
                        COLUMN_FILE_PATH + " TEXT NOT NULL, " +
                        COLUMN_FILE_NAME + " TEXT NOT NULL, " +
                        COLUMN_TIMING_AUDIO + " TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
