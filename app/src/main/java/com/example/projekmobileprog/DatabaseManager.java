package com.example.projekmobileprog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    private DatabaseHelper databaseHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context c) {
        context = c;
    }

    public DatabaseManager open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public void insertUser(String nameUser,  String usernameUser, String passwordUser) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME_USER, nameUser);
        contentValues.put(DatabaseHelper.COLUMN_USERNAME_USER, usernameUser);
        contentValues.put(DatabaseHelper.COLUMN_PASSWORD_USER, passwordUser);
        database.insert(DatabaseHelper.TABLE_NAME_USER, null, contentValues);
    }

    public Cursor getAllUsers() {
        String[] columns = new String[]{DatabaseHelper.COLUMN_ID_USER, DatabaseHelper.COLUMN_NAME_USER, DatabaseHelper.COLUMN_USERNAME_USER, DatabaseHelper.COLUMN_PASSWORD_USER};
        return database.query(DatabaseHelper.TABLE_NAME_USER, columns, null, null, null, null, null);
    }

    public int getIdUserbyUsername(String username) {
        int userId = -1;
        String[] columns = {DatabaseHelper.COLUMN_ID_USER};
        String selection = DatabaseHelper.COLUMN_USERNAME_USER + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NAME_USER, columns, selection, selectionArgs, null, null, null
        );
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_USER));
            cursor.close();
        }
        return userId;
    }


    public Map<String, String> getUserbyIdUser(int userId) {
        Map<String, String> userMap = new HashMap<>();
        String[] columns = {
                DatabaseHelper.COLUMN_ID_USER, DatabaseHelper.COLUMN_NAME_USER, DatabaseHelper.COLUMN_USERNAME_USER, DatabaseHelper.COLUMN_PASSWORD_USER
        };
        String selection = DatabaseHelper.COLUMN_ID_USER + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_NAME_USER, columns, selection, selectionArgs, null, null, null
        );
        if (cursor != null && cursor.moveToFirst()) {
            userMap.put("id_user", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_USER)));
            userMap.put("name_user", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_USER)));
            userMap.put("username_user", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME_USER)));
            userMap.put("password_user", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD_USER)));
            cursor.close();
        }
        return userMap;
    }

    public void updateUser(int idUser, String nameUser, String usernameUser, String passwordUser) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME_USER, nameUser);
        contentValues.put(DatabaseHelper.COLUMN_USERNAME_USER, usernameUser);
        contentValues.put(DatabaseHelper.COLUMN_PASSWORD_USER, passwordUser);
        String selection = DatabaseHelper.COLUMN_ID_USER + " = ?";
        String[] selectionArgs = {String.valueOf(idUser)};
        database.update(DatabaseHelper.TABLE_NAME_USER, contentValues, selection, selectionArgs);
    }

    private void insertCategory(String categoryName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME_CATEGORY, categoryName);
        database.insert(DatabaseHelper.TABLE_NAME_CATEGORY, null, contentValues);
    }

    public void defaultCategories() {
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_NAME_CATEGORY;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            insertCategory("Action");
            insertCategory("Adventure");
            insertCategory("Comedy");
            insertCategory("Drama");
            insertCategory("Fantasy");
            insertCategory("Historical");
            insertCategory("Horror");
            insertCategory("Mystery");
            insertCategory("Romance");
            insertCategory("Science Fiction");
            insertCategory("Thriller");
            insertCategory("Western");
            insertCategory("Sports");
        }
    }

    public Cursor getAllCategories() {
        String[] columns = new String[]{DatabaseHelper.COLUMN_ID_CATEGORY, DatabaseHelper.COLUMN_NAME_CATEGORY};
        return database.query(DatabaseHelper.TABLE_NAME_CATEGORY, columns, null, null, null, null, null);
    }

    public void insertNovel(String nameNovel, String bodyNovel, int idCategory, int idUser) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME_NOVEL, nameNovel);
        contentValues.put(DatabaseHelper.COLUMN_BODY_NOVEL, bodyNovel);
        contentValues.put(DatabaseHelper.COLUMN_ID_CATEGORY_FK, idCategory);
        contentValues.put(DatabaseHelper.COLUMN_ID_USER_FK, idUser);
        database.insert(DatabaseHelper.TABLE_NAME_NOVEL, null, contentValues);
    }

    public List<Map<String, String>> getAllNovels() {
        String query = "SELECT " +
                "n." + DatabaseHelper.COLUMN_ID_NOVEL + " AS novel_id, " +
                "n." + DatabaseHelper.COLUMN_NAME_NOVEL + " AS novel_name, " +
                "c." + DatabaseHelper.COLUMN_NAME_CATEGORY + " AS category_name, " +
                "u." + DatabaseHelper.COLUMN_NAME_USER + " AS user_name " +
                "FROM " + DatabaseHelper.TABLE_NAME_NOVEL + " n " +
                "JOIN " + DatabaseHelper.TABLE_NAME_CATEGORY + " c ON n." + DatabaseHelper.COLUMN_ID_CATEGORY_FK + " = c." + DatabaseHelper.COLUMN_ID_CATEGORY + " " +
                "JOIN " + DatabaseHelper.TABLE_NAME_USER + " u ON n." + DatabaseHelper.COLUMN_ID_USER_FK + " = u." + DatabaseHelper.COLUMN_ID_USER;

        Cursor cursor = database.rawQuery(query, null);
        List<Map<String, String>> novelList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Map<String, String> novelMap = new HashMap<>();
                novelMap.put("novel_id", cursor.getString(cursor.getColumnIndex("novel_id")));
                novelMap.put("novel_name", cursor.getString(cursor.getColumnIndex("novel_name")));
                novelMap.put("category_name", cursor.getString(cursor.getColumnIndex("category_name")));
                novelMap.put("user_name", cursor.getString(cursor.getColumnIndex("user_name")));
                novelList.add(novelMap);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return novelList;
    }

    public Cursor getAllNovelsByIdUser(int userId) {
        String query = "SELECT " +
                "n." + DatabaseHelper.COLUMN_ID_NOVEL + " AS _id, " +
                "n." + DatabaseHelper.COLUMN_NAME_NOVEL + " AS novel_name, " +
                "n." + DatabaseHelper.COLUMN_BODY_NOVEL + " AS novel_body, " +
                "c." + DatabaseHelper.COLUMN_NAME_CATEGORY + " AS category_name " +
                "FROM " + DatabaseHelper.TABLE_NAME_NOVEL + " n " +
                "JOIN " + DatabaseHelper.TABLE_NAME_CATEGORY + " c ON n." + DatabaseHelper.COLUMN_ID_CATEGORY_FK + " = c." + DatabaseHelper.COLUMN_ID_CATEGORY + " " +
                "WHERE n." + DatabaseHelper.COLUMN_ID_USER_FK + " = ?";

        String[] selectionArgs = new String[]{String.valueOf(userId)};
        return database.rawQuery(query, selectionArgs);
    }


    public void updateNovel(int idNovel, String nameNovel, String bodyNovel, int idCategory, int idUser) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME_NOVEL, nameNovel);
        contentValues.put(DatabaseHelper.COLUMN_BODY_NOVEL, bodyNovel);
        contentValues.put(DatabaseHelper.COLUMN_ID_CATEGORY_FK, idCategory);
        contentValues.put(DatabaseHelper.COLUMN_ID_USER_FK, idUser);
        String selection = DatabaseHelper.COLUMN_ID_NOVEL + " = ?";
        String[] selectionArgs = {String.valueOf(idNovel)};
        database.update(DatabaseHelper.TABLE_NAME_NOVEL, contentValues, selection, selectionArgs);
    }

    public List<Map<String, String>> getNovelByIdNovel(int idNovel) {
        String query = "SELECT " +
                "n." + DatabaseHelper.COLUMN_NAME_NOVEL + " AS novel_name, " +
                "n." + DatabaseHelper.COLUMN_BODY_NOVEL + " AS novel_body, " +
                "c." + DatabaseHelper.COLUMN_NAME_CATEGORY + " AS category_name, " +
                "u." + DatabaseHelper.COLUMN_NAME_USER + " AS user_name, " +
                "n." + DatabaseHelper.COLUMN_ID_USER_FK + " AS user_id_fk " +
                "FROM " + DatabaseHelper.TABLE_NAME_NOVEL + " n " +
                "JOIN " + DatabaseHelper.TABLE_NAME_CATEGORY + " c ON n." + DatabaseHelper.COLUMN_ID_CATEGORY_FK + " = c." + DatabaseHelper.COLUMN_ID_CATEGORY + " " +
                "JOIN " + DatabaseHelper.TABLE_NAME_USER + " u ON n." + DatabaseHelper.COLUMN_ID_USER_FK + " = u." + DatabaseHelper.COLUMN_ID_USER + " " +
                "WHERE n." + DatabaseHelper.COLUMN_ID_NOVEL + " = ?";

        String[] selectionArgs = new String[]{String.valueOf(idNovel)};
        Cursor cursor = database.rawQuery(query, selectionArgs);
        List<Map<String, String>> novelList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Map<String, String> novelMap = new HashMap<>();
                novelMap.put("novel_name", cursor.getString(cursor.getColumnIndex("novel_name")));
                novelMap.put("novel_body", cursor.getString(cursor.getColumnIndex("novel_body")));
                novelMap.put("category_name", cursor.getString(cursor.getColumnIndex("category_name")));
                novelMap.put("user_name", cursor.getString(cursor.getColumnIndex("user_name")));
                novelMap.put("user_id_fk", cursor.getString(cursor.getColumnIndex("user_id_fk")));
                novelList.add(novelMap);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return novelList;
    }


    public void deleteNovel(int idNovel) {
        database.delete(DatabaseHelper.TABLE_NAME_NOVEL, DatabaseHelper.COLUMN_ID_NOVEL + "=" + idNovel, null);
    }

    public boolean validationLogin(String username, String password) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_USERNAME_USER,
                DatabaseHelper.COLUMN_PASSWORD_USER
        };

        String selection = DatabaseHelper.COLUMN_USERNAME_USER + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD_USER + " = ?";
        String[] selectionArgs = { username, password };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME_USER, projection, selection, selectionArgs, null, null, null
        );
        boolean dataValidationLogin = cursor.moveToFirst();
        cursor.close();

        return dataValidationLogin;
    }

    public boolean validationUsername(String username) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = { DatabaseHelper.COLUMN_USERNAME_USER };

        String selection = DatabaseHelper.COLUMN_USERNAME_USER + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME_USER, projection, selection, selectionArgs, null, null, null
        );
        boolean dataValidationUsername = cursor.moveToFirst();
        cursor.close();

        return dataValidationUsername;
    }

    public List<String> getSpinCategories() {
        List<String> categories = new ArrayList<>();
        String selectQuery = "SELECT " + DatabaseHelper.COLUMN_ID_CATEGORY + ", " + DatabaseHelper.COLUMN_NAME_CATEGORY + " FROM " + DatabaseHelper.TABLE_NAME_CATEGORY;

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getInt(0) + " | " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }
}
