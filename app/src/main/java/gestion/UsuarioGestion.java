package gestion;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import data.AdminDB;
import model.Usuario;

public class UsuarioGestion {

    private static AdminDB data = null; //Enlace a la base de datos física
    private static SQLiteDatabase conexion = null; // Conexion a la base de datos..

    public static void init(AdminDB data) {
        UsuarioGestion.data = data;
    }//fin del init

    public static boolean inserta(Usuario usuario) {
        long resultado = -1;
        if (usuario!=null) {
            ContentValues registro = new ContentValues();
            registro.put("nombre",usuario.getNombre());
            registro.put("apellido",usuario.getApellido());
            registro.put("edad",usuario.getEdad());
            registro.put("telefono",usuario.getTelefono());
            registro.put("correo",usuario.getCorreo());
            conexion = data.getWritableDatabase();
            resultado=conexion.insert("usuario",null,registro);
            conexion.close();
        }
        return resultado != -1;
    }//fin de la insercion

    public static Usuario buscar(String correo) {
        Usuario usuario = null;
        data.toString();
        conexion = data.getReadableDatabase();

        Cursor datos = conexion.rawQuery(
                "select * from usuario where correo=?",
                new String[]{""+correo+""});

        if (datos.moveToFirst()) {
            usuario = new Usuario(
                    datos.getString(0),
                    datos.getString(1),
                    datos.getString(2),
                    datos.getInt(3),
                    datos.getString(4),
                    datos.getString(5),
                    datos.getString(6));
        }
        conexion.close();
        return usuario;
    }//fin de la busqueda

}//fin de la clase
