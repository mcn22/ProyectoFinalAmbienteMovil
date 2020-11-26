package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminDB extends SQLiteOpenHelper {
    //Constructor que creará la base de datos inicialmente...
    public AdminDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Este método se ejecuta una UNICA vez...
    // cuando se crea por primera vez la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table usuario (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre text, apellido text, edad int, telefono text, correo text)");
    }

    //Este método se ejecuta una UNICA vez...
    // cuando se cambia la versión de la base de datosS
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("create table usuario (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre text, apellido text, edad int, telefono text, correo text)");
    }
}//fin de la clase
