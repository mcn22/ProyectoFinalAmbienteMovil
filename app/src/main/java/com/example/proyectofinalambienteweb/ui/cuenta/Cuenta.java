package com.example.proyectofinalambienteweb.ui.cuenta;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalambienteweb.MainActivity;
import com.example.proyectofinalambienteweb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Usuario;

public class Cuenta extends Fragment {

    private Button logout;
    private Button editar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference referencia;

    private TextView tvNombre;
    private TextView tvApellido;
    private TextView tvEdad;
    private TextView tvTelefono;
    private TextView tvCorreo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cuenta, container, false);
        //mapeado
        tvNombre = root.findViewById(R.id.tvNombrePrincipal);
        tvApellido = root.findViewById(R.id.tvApellido);
        tvEdad = root.findViewById(R.id.tvEdad);
        tvTelefono = root.findViewById(R.id.tvTelefono);
        tvCorreo = root.findViewById(R.id.tvCorreo);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        logout = root.findViewById(R.id.btLogout);
        editar = root.findViewById(R.id.btEditar);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               editar();
            }
        });

        referencia = FirebaseDatabase.getInstance().getReference("usuarios/user_"+mAuth.getCurrentUser().getUid());
        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                dibuja(usuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error...", Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }//fin del onCreate

    private void editar() {
        Intent intent = new Intent(this.getContext(), EditarDatos.class);
        startActivity(intent);
    }//fin de editar

    private void dibuja(Usuario usuario) {
        tvNombre.setText(usuario.getNombre());
        tvApellido.setText(usuario.getApellido());
        tvEdad.setText(String.valueOf(usuario.getEdad()));
        tvTelefono.setText(usuario.getTelefono());
        tvCorreo.setText(usuario.getCorreo());
}//fin de dibuja

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }//fin del logout

}//fin de la clase