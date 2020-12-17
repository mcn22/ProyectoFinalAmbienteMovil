package com.example.proyectofinalambienteweb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.proyectofinalambienteweb.adapter.CitaAdapter;
import com.example.proyectofinalambienteweb.data.AdminDB;
import com.example.proyectofinalambienteweb.gestion.CitaGestion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import model.Cita;
import model.Usuario;

public class activity_menuprincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference referencia;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;

    private RecyclerView recyclerView;
    private CitaAdapter citaAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Cita> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuprincipal);
        mAuth = FirebaseAuth.getInstance();
        referencia = FirebaseDatabase.getInstance().getReference("usuarios/user_"+mAuth.getCurrentUser().getUid());
        storageRef = FirebaseStorage.getInstance().getReference();
        AdminDB data = new AdminDB(getApplicationContext(),"FilaVirtual.db",null,1);
        CitaGestion.init(data);
        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                TextView txtName = findViewById(R.id.tvNombrePrincipal);
                final ImageView img = findViewById(R.id.imgPrincipal);

                String urlImage = usuario.getImagen();
                if (!urlImage.equals("")) {
                    storageRef.child(urlImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(activity_menuprincipal.this).load(uri.toString()).asBitmap().centerCrop().into(new BitmapImageViewTarget(img) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(activity_menuprincipal.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    img.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }
                txtName.setText("Hola " + usuario.getNombre() + " " + usuario.getApellido() + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_menuprincipal.this, "Error...", Toast.LENGTH_SHORT).show();
            }
        });//fin de la referencia
        //Trabajo con el reciclador
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(activity_menuprincipal.this);
        recyclerView.setLayoutManager(layoutManager);

        try {
            if(CitaGestion.getCitas(mAuth.getUid()) != null){
                lista = CitaGestion.getCitas(mAuth.getUid());
            }

        }catch (NullPointerException e){

        }
        citaAdapter = new CitaAdapter(lista);

        recyclerView.setAdapter(citaAdapter);

        citaAdapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Coloco en posicion la posicion en el recycler view que le di clic
                int posicion=recyclerView.getChildAdapterPosition(v);
                Cita cita = lista.get(posicion);
                Intent intent= new Intent(activity_menuprincipal.this, activity_editarcita.class);
                intent.putExtra("cita",cita);
                startActivity(intent);
            }
        });


    }//fin del onCreate

    public void goToEdit(View v){
        Intent intent = new Intent(activity_menuprincipal.this, activity_infopersonal.class);
        startActivity(intent);
    }

    public void goToCreate(View v){
        Intent intent = new Intent(activity_menuprincipal.this, activity_nuevacita.class);
        intent.putExtra("UserID", mAuth.getUid());
        startActivity(intent);
    }

}//fin de la clase