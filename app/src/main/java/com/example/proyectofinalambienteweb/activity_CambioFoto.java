package com.example.proyectofinalambienteweb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;

import model.Usuario;

public class activity_CambioFoto extends AppCompatActivity {

    private Button btBusca;
    private Button btActualizaFoto;
    private ImageView imagen;

    private DatabaseReference referencia;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__cambio_foto);

        imagen = findViewById(R.id.imageViewCambio);
        btBusca = findViewById(R.id.btCancela);
        btActualizaFoto = findViewById(R.id.btCreaCita);

        mAuth = FirebaseAuth.getInstance();
        referencia = FirebaseDatabase.getInstance().getReference("usuarios/user_"+mAuth.getCurrentUser().getUid());
        storageRef = FirebaseStorage.getInstance().getReference();

        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                String urlImage = usuario.getImagen();
                if (!urlImage.equals("")) {
                    storageRef.child(urlImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(activity_CambioFoto.this).load(uri.toString()).asBitmap().centerCrop().into(new BitmapImageViewTarget(imagen) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(activity_CambioFoto.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    imagen.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_CambioFoto.this, "Error...", Toast.LENGTH_SHORT).show();
            }
        });

        btBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

        btActualizaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subir();
            }
        });
    }

    private void buscar() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent,333);
    }//fin de buscar

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null){
            super.onActivityResult(requestCode, resultCode, data);
            uri = data.getData();
            imagen.setImageURI(uri);
        }
    }

    private void subir() {
        String archivo = "img_"+mAuth.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance()
                .getReference().child(archivo);
        storageRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(activity_CambioFoto.this, "Se subió...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity_CambioFoto.this, activity_menuprincipal.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity_CambioFoto.this, "Error cargando...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void goToMenu(View v){
        Intent intent = new Intent(activity_CambioFoto.this, activity_infopersonal.class);
        startActivity(intent);
    }//fin del goToMenu


}