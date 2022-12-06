package com.example.isss.GTP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isss.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Kegiatan extends AppCompatActivity {

    //atribut
    EditText eLokasi,eDeskripsi;
    Button bKirim;

    //firebase
    FirebaseFirestore dbs;

    //date firebase
    Timestamp fwaktu = Timestamp.now();

    //String
    String displayName,idDocument;

    //loading
    ProgressDialog progress;

    //currentLocation
    FusedLocationProviderClient fusedLocationClient;

    //gfoto
    GridLayout gFoto;

    //foto
    RecyclerView rFoto;
    private StorageReference mStorageReference;
    List<ModalClass> modalClassList;
    CustomAdapter customAdapter;
    StorageReference storageReference;

    //CameraCode
    private static final int PICK_FROM_GALLERY = 107;
    private static final int IMAGE_CODE = 1;

    //array
    ArrayList listImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan);


        //foto
        rFoto = findViewById(R.id.recyclerFoto);
        modalClassList = new ArrayList<>();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("Photo");
        storageReference = FirebaseStorage.getInstance().getReference();

        //gfoto
        gFoto = findViewById(R.id.gFoto);
        gFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            fotoGallery();
            }
        });

        //loading
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        //firebase
        dbs = FirebaseFirestore.getInstance();

        //string
        idDocument = getIntent().getStringExtra("idDoc");
        displayName = getIntent().getStringExtra("displayName");

        //atribut
        eLokasi = findViewById(R.id.eLocation);
        eDeskripsi = findViewById(R.id.eDesc);

        bKirim = findViewById(R.id.kirim);
        bKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String getLokasi = eLokasi.getText().toString();
               String getDeskripsi = eDeskripsi.getText().toString();

                if (getLokasi.isEmpty()&& getDeskripsi.isEmpty()&& listImage.isEmpty()) {
                    eLokasi.setError("Tidak boleh kosong !");
                    eDeskripsi.setError("Tidak boleh kosong !");
                    Toast.makeText(getApplicationContext(), "Foto Tidak Boleh kosong !", Toast.LENGTH_LONG).show();

                }
                else if (getLokasi.isEmpty()) {
                    eLokasi.setError("Tidak boleh kosong !");
                } else if (getDeskripsi.isEmpty()) {
                    eDeskripsi.setError("Tidak boleh kosong !");
                } else if (listImage.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Foto Tidak Boleh kosong !", Toast.LENGTH_LONG).show();
                } else {
                    progress.show();
                    //currentLocation
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            double CurrentLat = location.getLatitude();
                            double CurrentLng = location.getLongitude();

                            SendDataKegiatan sendDataMultimedia = new SendDataKegiatan(
                                    eDeskripsi.getText().toString(),
                                    listImage,
                                    fwaktu,
                                    displayName,
                                    eLokasi.getText().toString(),
                                    CurrentLat,
                                    CurrentLng
                            );

                            dbs.collection("Kegiatan").document().set(sendDataMultimedia)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progress.dismiss();

                                                Toast.makeText(getApplicationContext(), "Lapor kegiatan berhasil masuk!", Toast.LENGTH_LONG).show();

                                                Intent i = new Intent(Kegiatan.this, Home_Gtp.class);
                                                i.putExtra("idDoc", idDocument);
                                                i.putExtra("displayName",displayName);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    });
                }

            }
        });

    }

    private void fotoGallery() {
        if (checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            startActivityForResult(intent,IMAGE_CODE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {
                Log.d("dataFoto", "multiple");

                rFoto.removeAllViews();
                rFoto.setHasFixedSize(true);
                rFoto.setLayoutManager(new LinearLayoutManager(this));

                listImage = new ArrayList<>();

                int totalitem = data.getClipData().getItemCount();
                Log.d("totalFoto", String.valueOf(totalitem));

                for (int i = 0; i < totalitem; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String imagename = getFileName(imageUri);
                    ModalClass modalClass = new ModalClass(imagename, imageUri);

                    modalClassList.add(modalClass);

                    customAdapter = new CustomAdapter(Kegiatan.this, modalClassList);
                    rFoto.setAdapter(customAdapter);

                    StorageReference mRef = mStorageReference.child(imagename);
                    mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(Induction.this, "Done", Toast.LENGTH_SHORT).show();
                            listImage.add(imagename);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Kegiatan.this, "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }else if (data.getData() != null) {

                Log.d("dataFoto", "single");
                rFoto.removeAllViews();
                rFoto.setHasFixedSize(true);
                rFoto.setLayoutManager(new LinearLayoutManager(this));

                listImage = new ArrayList<>();

                Uri imageUri = data.getData();
                listImage.add(imageUri);
                String imagename = getFileName(imageUri);
                ModalClass modalClass = new ModalClass(imagename, imageUri);

                modalClassList.add(modalClass);

                customAdapter = new CustomAdapter(Kegiatan.this, modalClassList);
                rFoto.setAdapter(customAdapter);

                StorageReference mRef = mStorageReference.child(imagename);
                mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        listImage.add(imagename);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Kegiatan.this, "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }
}