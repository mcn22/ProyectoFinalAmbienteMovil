package com.example.proyectofinalambienteweb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Usuario;

public class activity_infopersonal extends AppCompatActivity {

    private EditText etNombre;
    private EditText etApellido;
    private EditText etEdad;
    private EditText etTelefono;
    private Button btCambiarDatos;
    private Button btCambiarFoto;
    private Button btCerrarSesion;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference referencia;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infopersonal);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEdad = findViewById(R.id.etFecha);
        etTelefono = findViewById(R.id.etTelefono);
        btCambiarDatos = findViewById(R.id.btCreaCita);
        btCambiarFoto = findViewById(R.id.btCancela);
        btCerrarSesion = findViewById(R.id.btCerrarSesion);

        btCambiarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambioDatos();
            }
        });

        btCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambioFoto();
            }
        });

        btCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        referencia = FirebaseDatabase.getInstance().getReference("usuarios/user_"+mAuth.getCurrentUser().getUid());
        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);
                dibuja(usuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_infopersonal.this, "Error...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dibuja(Usuario usuario) {
        etNombre.setText(usuario.getNombre());
        etApellido.setText(usuario.getApellido());
        etEdad.setText(String.valueOf(usuario.getEdad()));
        etTelefono.setText(String.valueOf(usuario.getTelefono()));
    }//fin de dibuja

    private void cambioDatos() {
        String nombre = etNombre.getText().toString();
        String apellido = etApellido.getText().toString();
        String edad = etEdad.getText().toString();
        String telefono = etTelefono.getText().toString();
        Usuario usuario = new Usuario(mAuth.getCurrentUser().getUid(),
                nombre,
                apellido,
                Integer.parseInt(edad),
                telefono,
                mAuth.getCurrentUser().getEmail(), "img_"+mAuth.getCurrentUser().getUid()
        );
        referencia.setValue(usuario);
        Toast.makeText(activity_infopersonal.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
    }//fin de cambio de datos

    private void cambioFoto() {
        Intent intent = new Intent(activity_infopersonal.this, activity_CambioFoto.class);
        startActivity(intent);
    }//fin de cambio de datos

    private void cerrarSesion() {
        mAuth.signOut();
        Intent intent = new Intent(activity_infopersonal.this, MainActivity.class);
        startActivity(intent);
    }//fin de cambio de datos

    public void goToMenu(View v){
        Intent intent = new Intent(activity_infopersonal.this, activity_menuprincipal.class);
        startActivity(intent);
    }//fin del goToMenu
}