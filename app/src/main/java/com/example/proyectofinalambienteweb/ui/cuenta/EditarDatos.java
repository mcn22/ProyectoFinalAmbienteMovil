package com.example.proyectofinalambienteweb.ui.cuenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofinalambienteweb.MainActivity;
import com.example.proyectofinalambienteweb.Principal;
import com.example.proyectofinalambienteweb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Usuario;

public class EditarDatos extends AppCompatActivity {

    private EditText etNombre;
    private EditText etApellido;
    private EditText etEdad;
    private EditText etTelefono;
    private Button btConfirmar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference referencia;

    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_datos);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEdad = findViewById(R.id.etEdad);
        etTelefono = findViewById(R.id.etTelefono);
        btConfirmar = findViewById(R.id.btConfirmar);

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambioDatos();
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
                Toast.makeText(EditarDatos.this, "Error...", Toast.LENGTH_SHORT).show();
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
                mAuth.getCurrentUser().getEmail(), ""
        );
        referencia.setValue(usuario);
        Toast.makeText(EditarDatos.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
    }//fin de cambio de datos
}