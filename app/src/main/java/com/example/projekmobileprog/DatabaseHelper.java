package com.example.projekmobileprog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dbnovel.db";
    public static final int DATABASE_VERSION = 8;

    public static final String TABLE_NAME_USER = "users";
    public static final String COLUMN_ID_USER = "_id";
    public static final String COLUMN_NAME_USER = "name";
    public static final String COLUMN_USERNAME_USER = "username";
    public static final String COLUMN_PASSWORD_USER = "password";

    private static final String TABLE_CREATE_USER =
            "CREATE TABLE " + TABLE_NAME_USER + " (" +
                    COLUMN_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_USER + " TEXT NOT NULL, " +
                    COLUMN_USERNAME_USER + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_PASSWORD_USER + " TEXT NOT NULL);";

    public static final String TABLE_NAME_CATEGORY = "categories";
    public static final String COLUMN_ID_CATEGORY = "_id";
    public static final String COLUMN_NAME_CATEGORY = "name";

    private static final String TABLE_CREATE_CATEGORY =
            "CREATE TABLE " + TABLE_NAME_CATEGORY + " (" +
                    COLUMN_ID_CATEGORY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_CATEGORY + " TEXT UNIQUE NOT NULL);";

    public static final String TABLE_NAME_NOVEL = "novels";
    public static final String COLUMN_ID_NOVEL = "_id";
    public static final String COLUMN_NAME_NOVEL = "name";
    public static final String COLUMN_BODY_NOVEL = "body";
    public static final String COLUMN_ID_CATEGORY_FK = "category_id";
    public static final String COLUMN_ID_USER_FK = "user_id";

    private static final String TABLE_CREATE_NOVEL =
            "CREATE TABLE " + TABLE_NAME_NOVEL + " (" +
                    COLUMN_ID_NOVEL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_NOVEL + " TEXT NOT NULL, " +
                    COLUMN_BODY_NOVEL + " TEXT NOT NULL, " +
                    COLUMN_ID_CATEGORY_FK + " INTEGER, " +
                    COLUMN_ID_USER_FK + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_ID_CATEGORY_FK + ") REFERENCES " + TABLE_NAME_CATEGORY + "(" + COLUMN_ID_CATEGORY + "), " +
                    "FOREIGN KEY(" + COLUMN_ID_USER_FK + ") REFERENCES " + TABLE_NAME_USER + "(" + COLUMN_ID_USER + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USER);
        db.execSQL(TABLE_CREATE_CATEGORY);
        db.execSQL(TABLE_CREATE_NOVEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOVEL);
        onCreate(db);
    }
}
