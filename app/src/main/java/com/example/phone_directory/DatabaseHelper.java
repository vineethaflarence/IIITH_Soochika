package com.example.phone_directory;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "my_database.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE model (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Upgrade database schema.
    }

    public void insertModel(model model) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("body", model.getBody());
        values.put("email", model.getEmail());
        values.put("department", model.getDepartment());
        db.insert("model", null, values);
        db.close();
    }

    public List<model> getContacts() {
        List<model> contacts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM model", null);
        while (cursor.moveToNext()) {
            model model = new model();
            model.setBody(cursor.getString(1));
            model.setEmail(cursor.getString(2));
            model.setMobile(cursor.getString(3));
            contacts.add(model);
        }
        cursor.close();
        db.close();
        return contacts;
    }

    public void updateContact(model model) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Body", model.getBody());
        values.put("email", model.getEmail());
        values.put("phone", model.getMobile());
        //db.update("contacts", values, "id = ?", new String[]{String.valueOf(model.getId())});
        db.close();
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = getWritableDatabase();
        //db.delete("contacts", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
