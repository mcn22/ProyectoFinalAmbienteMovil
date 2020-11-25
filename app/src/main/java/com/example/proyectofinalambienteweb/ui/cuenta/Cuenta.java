package com.example.proyectofinalambienteweb.ui.cuenta;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.proyectofinalambienteweb.MainActivity;
import com.example.proyectofinalambienteweb.R;
import com.example.proyectofinalambienteweb.Registro;
import com.google.firebase.auth.FirebaseAuth;

public class Cuenta extends Fragment {

    private Button logout;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cuenta, container, false);
        mAuth = FirebaseAuth.getInstance();

        logout = root.findViewById(R.id.btLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return root;
    }//fin del onCreate

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }//fin del logout

}//fin de la clase