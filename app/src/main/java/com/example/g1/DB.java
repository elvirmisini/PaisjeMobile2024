package com.example.g1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {

public static final String DBNAME="login.db";

    public DB(@Nullable Context context) {
        super(context,"login.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table users(email TEXT PRIMARY KEY, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists users");
    }

    public  Boolean insertData(String email, String password){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("email", email);
        contentValues.put("password",password);

        long result=db.insert("users",null,contentValues);
        if(result==-1) return false;
        else return true;
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select * from users where email=?",new String[]{email});
        if (cursor.getCount()>0){
            return true;
        }else return false;

    }
}