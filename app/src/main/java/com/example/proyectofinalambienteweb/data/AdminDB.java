package com.example.proyectofinalambienteweb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminDB extends SQLiteOpenHelper {
    public AdminDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table citas (idCita INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, UserID text, establecimiento text, area text,fecha text,hora text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("create table citas (idCita INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, UserID text, establecimiento text, area text,fecha text,hora text)");
    }
}
