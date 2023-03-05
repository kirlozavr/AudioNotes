package com.example.audionotes.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.audionotes.entity.AudioNoteEntity;

import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {

    private SQLiteDatabase dataBase;
    private DataBaseHelper dataBaseHelper;

    public DataBaseManager(Context context){
        dataBaseHelper = new DataBaseHelper(context);
    }

    public void open(){
        try {
            dataBase = dataBaseHelper.getWritableDatabase();
        } catch (Exception e){
            dataBase = dataBaseHelper.getReadableDatabase();
        }
    }

    public void close(){
        dataBaseHelper.close();
    }

    public List<AudioNoteEntity> getAllEntity(){
        List<AudioNoteEntity> allEntity = new ArrayList<>();
        Cursor query = dataBase.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + ";", null);
        while (query.moveToNext()){
            AudioNoteEntity noteEntity = new AudioNoteEntity(
                    query.getLong(query.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ID)),
                    query.getString(query.getColumnIndexOrThrow(DataBaseHelper.COLUMN_TITLE_NOTES)),
                    query.getString(query.getColumnIndexOrThrow(DataBaseHelper.COLUMN_DATE_CREATE_NOTES)),
                    query.getString(query.getColumnIndexOrThrow(DataBaseHelper.COLUMN_TIME_CREATE_NOTES)),
                    query.getString(query.getColumnIndexOrThrow(DataBaseHelper.COLUMN_FILE_PATH)),
                    query.getString(query.getColumnIndexOrThrow(DataBaseHelper.COLUMN_FILE_NAME)),
                    query.getInt(query.getColumnIndexOrThrow(DataBaseHelper.COLUMN_TIMING_AUDIO))
            );
            allEntity.add(noteEntity);
        }
        query.close();
        return allEntity;
    }

    public void insertEntity(AudioNoteEntity noteEntity){
        ContentValues content = new ContentValues();
        content.put(DataBaseHelper.COLUMN_TITLE_NOTES, noteEntity.getTitleNote());
        content.put(DataBaseHelper.COLUMN_DATE_CREATE_NOTES, noteEntity.getDateCreateNote());
        content.put(DataBaseHelper.COLUMN_TIME_CREATE_NOTES, noteEntity.getTimeCreateNote());
        content.put(DataBaseHelper.COLUMN_FILE_PATH, noteEntity.getFilePath());
        content.put(DataBaseHelper.COLUMN_FILE_NAME, noteEntity.getFileName());
        content.put(DataBaseHelper.COLUMN_TIMING_AUDIO, noteEntity.getTimingAudio());
        dataBase.insert(DataBaseHelper.TABLE_NAME, null, content);
    }

    public void deleteEntity(long id){
        dataBase.delete(
                DataBaseHelper.TABLE_NAME,
                "_id = ?",
                new String[]{String.valueOf(id)}
        );
    }

}
