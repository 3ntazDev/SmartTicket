package com.example.smartticket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role"; // جديد: لتحديد نوع المستخدم

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_ROLE + " TEXT);"; // إضافة عمود الدور

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        addInitialUsers(db);
    }

    private void addInitialUsers(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // إضافة مستخدمين مع تحديد الدور
        values.put(COLUMN_USERNAME, "admin");
        values.put(COLUMN_PASSWORD, "1234");
        values.put(COLUMN_ROLE, "admin"); // الدور كـ "admin"
        db.insert(TABLE_USERS, null, values);

        values.put(COLUMN_USERNAME, "user1");
        values.put(COLUMN_PASSWORD, "password1");
        values.put(COLUMN_ROLE, "user"); // الدور كـ "user"
        db.insert(TABLE_USERS, null, values);

        values.put(COLUMN_USERNAME, "user2");
        values.put(COLUMN_PASSWORD, "password2");
        values.put(COLUMN_ROLE, "user"); // الدور كـ "user"
        db.insert(TABLE_USERS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // التحقق من بيانات الدخول
    public String getUserRole(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ROLE + " FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] { username, password });

        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndex(COLUMN_ROLE));
            cursor.close();
            return role; // إرجاع دور المستخدم (admin أو user)
        }
        cursor.close();
        return null; // لم يتم العثور على المستخدم
    }
}
