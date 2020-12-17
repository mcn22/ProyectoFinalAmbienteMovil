package com.example.proyectofinalambienteweb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.navigation.ui.AppBarConfiguration;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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

import model.Usuario;

public class menuprincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference referencia;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuprincipal);
        mAuth = FirebaseAuth.getInstance();
        referencia = FirebaseDatabase.getInstance().getReference("usuarios/user_"+mAuth.getCurrentUser().getUid());
        storageRef = FirebaseStorage.getInstance().getReference();

        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                TextView txtName = findViewById(R.id.tvNombrePrincipal);
                final ImageView imageView = findViewById(R.id.imgPrincipal);

                String urlImage = usuario.getImagen();
                if (!urlImage.equals("")) {
                    storageRef.child(urlImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(menuprincipal.this).load(uri.toString()).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(menuprincipal.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    imageView.setImageDrawable(circularBitmapDrawable);
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
                Toast.makeText(menuprincipal.this, "Error...", Toast.LENGTH_SHORT).show();
            }
        });
    }//fin del onCreate
}//fin de la clase