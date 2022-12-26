package com.example.isss.SI.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.isss.R;
import com.example.isss.SI.Side_SI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DoneDetail extends AppCompatActivity {
    String documentClickId;
    //firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Collection hasiltemplates
    CollectionReference df = db.collection("si").document("data_inspections").collection("inspections");

    TextView title,description,team,waktu,temperatur,kordinat,lokasi,status,signature;
    ImageView signatureImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_detail);

        documentClickId = getIntent().getStringExtra("doc");
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        team = findViewById(R.id.team);
        waktu = findViewById(R.id.time);
        temperatur = findViewById(R.id.temperatur);
        kordinat = findViewById(R.id.kordinat);
        lokasi = findViewById(R.id.lokasiInspeksi);
        status = findViewById(R.id.status);
        signature = findViewById(R.id.signature);
        signatureImg = findViewById(R.id.signatureImg);

        df.document(documentClickId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                String author = (String) task.getResult().get("author");
                String authorId = (String) task.getResult().get("authorId");
                String templateAddress = (String) task.getResult().get("templateAddress");
                Timestamp templateDate = (Timestamp) task.getResult().get("templateDate");
                String templateDescription = (String) task.getResult().get("templateDescription");
                String templateGroup = (String) task.getResult().get("templateGroup");
                String templateLocation = (String) task.getResult().get("templateLocation");
                String templateTeam = (String) task.getResult().get("templateTeam");
                String templateTemperature = (String) task.getResult().get("templateTemperature");
                String templateTitle = (String) task.getResult().get("templateTitle");
                String statusInspeksi = (String) task.getResult().get("status");
                String signatureId = (String) task.getResult().get("signature");
                Log.d("title",templateTitle);


                title.setText("Title Inspeksi : "+"\n"+templateTitle);
                description.setText("Description : "+"\n"+templateDescription);
                team.setText("Team Inspeksi : "+"\n"+templateTeam +" -"+ templateGroup);
                waktu.setText("Waktu Inspeksi : "+"\n"+templateDate.toDate());
                temperatur.setText("Temperatur Saat Inspeksi : "+"\n"+templateTemperature);
                kordinat.setText("Kordinat Inspeksi : "+"\n"+templateLocation);
                lokasi.setText("Lokasi Inspeksi : "+"\n"+templateAddress);
                status.setText("Status Inspeksi :"+"\n"+statusInspeksi);
                signature.setText("Signature :"+"\n");

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference photoReference= storageReference.child("Signature/"+signatureId);
                final long ONE_MEGABYTE = 1024 * 1024;
                photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        signatureImg.setImageBitmap(bmp);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Side_SI.class);
        startActivity(intent);
    }
}