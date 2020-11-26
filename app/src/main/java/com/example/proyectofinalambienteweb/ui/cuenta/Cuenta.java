package com.example.proyectofinalambienteweb.ui.cuenta;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalambienteweb.MainActivity;
import com.example.proyectofinalambienteweb.R;
import com.example.proyectofinalambienteweb.Registro;
import com.google.firebase.auth.FirebaseAuth;

import gestion.UsuarioGestion;
import model.Usuario;

public class Cuenta extends Fragment {

    private Button logout;
    private FirebaseAuth mAuth;

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
        tvNombre = root.findViewById(R.id.tvNombre);
        tvApellido = root.findViewById(R.id.tvApellido);
        tvEdad = root.findViewById(R.id.tvEdad);
        tvTelefono = root.findViewById(R.id.tvTelefono);
        tvCorreo = root.findViewById(R.id.tvCorreo);

        mAuth = FirebaseAuth.getInstance();
        recuperaDatosUsuario(mAuth.getCurrentUser().getEmail());
        logout = root.findViewById(R.id.btLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return root;
    }//fin del onCreate

    private void recuperaDatosUsuario(String email) {
        Usuario usuario = null;
        String correo = email;
        if (!correo.isEmpty()) {
            usuario = UsuarioGestion.buscar(correo);
            if (usuario!=null) {
                dibuja(usuario);
            } else {
                Toast.makeText(getContext(), "Estudiante no encontrado", Toast.LENGTH_SHORT).show();
            }
        }
    }//fin del recuperado de los datos del usuario

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