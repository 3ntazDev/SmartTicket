package com.example.smartticket;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UserManager {

    private DatabaseHelper dbHelper;

    public UserManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // إضافة مستخدم جديد
    public void addUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
    }

    // التحقق من وجود مستخدم
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE " +
                DatabaseHelper.COLUMN_USERNAME + "=? AND " +
                DatabaseHelper.COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {username, password};

        return db.rawQuery(query, selectionArgs).getCount() > 0;
    }
}
