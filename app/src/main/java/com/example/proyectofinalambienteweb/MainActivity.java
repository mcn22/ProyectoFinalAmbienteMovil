package com.example.proyectofinalambienteweb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofinalambienteweb.data.AdminDB;
import com.example.proyectofinalambienteweb.gestion.CitaGestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import model.Cita;

public class MainActivity extends AppCompatActivity {

    private EditText etCorreo;
    private EditText etPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        etCorreo = findViewById(R.id.etCorreo);
        etPass = findViewById(R.id.etPass);

        AdminDB data = new AdminDB(getApplicationContext(),"FilaVirtual.db",null,1);
        CitaGestion.init(data);
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


    public void login(View view){
        String correo = etCorreo.getText().toString();
        String pass = etPass.getText().toString();
        if(!correo.equals("") && !pass.equals("")){
            mAuth.signInWithEmailAndPassword(correo,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }else {
                                Toast.makeText(MainActivity.this, "Error de login", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }//fin del else
                        }//fin del public void
                    });//fin del listener
        }else{
            Toast.makeText(MainActivity.this, "Debes completar los datos", Toast.LENGTH_SHORT).show();
        }
    }//fin de login

    public void sinCuenta(View view) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }//fin del sinCuenta
}//fin de la clase