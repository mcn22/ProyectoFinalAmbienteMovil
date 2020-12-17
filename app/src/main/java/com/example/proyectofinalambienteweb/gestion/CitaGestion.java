package com.example.proyectofinalambienteweb.gestion;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.proyectofinalambienteweb.data.*;

import java.util.ArrayList;

import model.Cita;

public class CitaGestion {
    private static AdminDB data=null;
    private static SQLiteDatabase conexion = null;

    public static void init(AdminDB data) {
        CitaGestion.data = data;
    }//fin del init

    public static boolean inserta(Cita cita) {
        long resultado = -1;
        if (cita!=null) {
            ContentValues registroCita = new ContentValues();
            registroCita.put("UserID",cita.getUserId());
            registroCita.put("establecimiento",cita.getEstablecimiento());
            registroCita.put("area",cita.getArea());
            registroCita.put("fecha",cita.getFecha());
            registroCita.put("hora",cita.getHora());
            conexion = data.getWritableDatabase();
            resultado=conexion.insert("citas",null,registroCita);
            conexion.close();
        }//fin del if
        return resultado != -1;
    }//fin de la insercion de la cita

    public static boolean elimina(int id) {
        conexion = data.getWritableDatabase();
        long registros = conexion.delete(
                "citas",
                "idCita=?",
                new String[]{""+id+""});
        conexion.close();
        return registros==1;
    }//fin de elimina

    public static boolean modifica(Cita cita) {
        long resultado = -1;
        if (cita!=null) {
            ContentValues registroCita = new ContentValues();
            registroCita.put("UserID",cita.getUserId());
            registroCita.put("establecimiento",cita.getEstablecimiento());
            registroCita.put("area",cita.getArea());
            registroCita.put("fecha",cita.getFecha());
            registroCita.put("hora",cita.getHora());
            conexion = data.getWritableDatabase();
            resultado=conexion.update("citas",
                    registroCita,
                    "idCita=?",
                    new String[]{""+cita.getIdCita()+""});
            conexion.close();
        }//fin del if
        return resultado == 1;
    }//fin de modificar la cita

    public static ArrayList<Cita> getCitas(String idUsuario) {
        ArrayList<Cita> lista = new ArrayList<>();
        String idUser = idUsuario;
        conexion = data.getReadableDatabase();
        Cursor registros = conexion.rawQuery(
                "select * from citas where UserId='" + idUser +"'",
                null);
        while (registros.moveToNext()) {
            lista.add(new Cita(registros.getInt(0),
                    registros.getString(1),
                    registros.getString(2),
                    registros.getString(3),
                    registros.getString(4),
                    registros.getString(5)));
        }
        conexion.close();
        return lista;
    }//fin de la obtencion de la lista de las citas

}//fin de la clase
