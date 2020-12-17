package com.example.proyectofinalambienteweb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Usuario;

public class Registro extends AppCompatActivity {

    private EditText etNombre;
    private EditText etApellido;
    private EditText etEdad;
    private EditText etTelefono;
    private EditText etCorreo;
    private EditText etPass;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference usuariosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usuariosRef = database.getReference("usuarios");

        //AdminDB data = new AdminDB(this,"baseUsuarios.db",null,1);
        //UsuarioGestion.init(data);

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEdad = findViewById(R.id.etFecha);
        etTelefono = findViewById(R.id.etCorreo);
        etCorreo = findViewById(R.id.etCorreo);
        etPass = findViewById(R.id.etPass);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }//fin del onStart

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            Intent intent = new Intent(this, activity_menuprincipal.class);
            startActivity(intent);
        }//fin del if
    }//fin del update UI

    public void registro(View view){
        final String nombre = etNombre.getText().toString();
        final String apellido = etApellido.getText().toString();
        final String edad = etEdad.getText().toString();
        final String telefono = etTelefono.getText().toString();
        final String correo = etCorreo.getText().toString();
        String pass = etPass.getText().toString();
        if(!nombre.equals("") && !apellido.equals("") && !edad.equals("") &&
                !telefono.equals("")&& !correo.equals("")&& !pass.equals("")){
            mAuth.createUserWithEmailAndPassword(correo,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                insertaUsuario(nombre, apellido,edad, telefono, correo, "img_"+user.getUid(), user.getUid());
                                updateUI(user);
                            }else {
                                Toast.makeText(Registro.this, "Error de registro", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }else{
            Toast.makeText(Registro.this, "Debes completar los datos", Toast.LENGTH_SHORT).show();
        }
    }//fin de registro

    private void insertaUsuario(String nombre, String apellido, String edad, String telefono, String correo, String imagen, String uId){
        Usuario usuario = new Usuario(uId,
                nombre,
                apellido,
                Integer.parseInt(edad),
                telefono,
                correo, imagen
        );
        String direccion = "user_" + uId;
        usuariosRef.child(direccion).setValue(usuario);
    }//fin del inserta
}//fin de la clase