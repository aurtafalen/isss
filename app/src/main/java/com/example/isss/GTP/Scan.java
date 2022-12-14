package com.example.isss.GTP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isss.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Scan extends AppCompatActivity {

    TextView idCheckpoint,titikKoordinat,lokasi,area,team,waktu;
    Button ttd;
    String ttdText,idDocument;
    double CurrentLat,CurrentLng;
    //currentLocation
    FusedLocationProviderClient fusedLocationClient;

    //signature
    SignatureView mSignaturePad;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 3;

    //loading
    ProgressDialog progress;

    //firebase
    FirebaseFirestore dbs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //firebase
        dbs = FirebaseFirestore.getInstance();

        //loading
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        //waktu
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date = dateFormat.format(Calendar.getInstance().getTime());

        //hasilScan
        idDocument = getIntent().getStringExtra("idDoc");
        String Id = getIntent().getStringExtra("id");
        String cekPointLat = getIntent().getStringExtra("checkpointLat");
        String cekPointLng = getIntent().getStringExtra("checkpointLng");
        String Location = getIntent().getStringExtra("location");
        String Area = getIntent().getStringExtra("area");
        String displayName = getIntent().getStringExtra("displayName");

        idCheckpoint = findViewById(R.id.tCheckpoint);
        idCheckpoint.setText(Id);

        titikKoordinat = findViewById(R.id.tKordinat);
        titikKoordinat.setText(cekPointLat+","+cekPointLng);

        lokasi = findViewById(R.id.tLokasi);
        lokasi.setText(Location);

        area = findViewById(R.id.tArea);
        area.setText(Area);

        team = findViewById(R.id.tTeam);
        team.setText(displayName);

        waktu = findViewById(R.id.tWaktu);
        waktu.setText(date);

        ttd = findViewById(R.id.btn_ttd);
        ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttd();
            }
        });
        //currentLocation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            CurrentLat = location.getLatitude();
                            CurrentLng = location.getLongitude();

                            Location locationScan = new Location("scan");
                            locationScan.setLatitude(Double.parseDouble(cekPointLat));
                            locationScan.setLongitude(Double.parseDouble(cekPointLng));

                            Location locationCurrent = new Location("current");
                            locationCurrent.setLatitude(CurrentLat);
                            locationCurrent.setLongitude(CurrentLng);
                            double distance = locationCurrent.distanceTo(locationScan);

                            Log.d("getDistance", String.valueOf(distance));
                            if (distance >100){
                                ttd.setVisibility(View.INVISIBLE);
                                Snackbar.make(findViewById(R.id.scan),"Anda terlalu jauh dari titik Check Point!",Snackbar.LENGTH_INDEFINITE)
                                        .setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        }).show();
                            }else{
                                ttd.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

    }
    void ttd() {
        progress.show();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Scan.this);
        alertDialog.setTitle("Tanda Tangan");

        mSignaturePad = new SignatureView(Scan.this, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mSignaturePad.setLayoutParams(lp);


        alertDialog.setView(mSignaturePad);

        alertDialog.setPositiveButton("Selesai",
                new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {


                        //uploadTtd
                        if (ContextCompat.checkSelfPermission(
                                getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Scan.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_STORAGE_PERMISSION);
                        } else {
                            Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

                            if (addJpgSignatureToGallery(signatureBitmap)) {

                                UploadSignatureToCloudStore(signatureBitmap);


                            }
                        }



                    }
                });

        alertDialog.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        String namefile = String.format("Signature_%d.jpg", System.currentTimeMillis());

        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), namefile);
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();

    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        Scan.this.sendBroadcast(mediaScanIntent);
    }

    private void UploadSignatureToCloudStore(Bitmap signatureBitmap) {
        final String namefile = String.format("Signature_%d.jpg", System.currentTimeMillis());
        ttdText = namefile;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("Signature/" + namefile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("DrawSignatureActivity", "Fail! upload images. error : " + exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                uploadSCan();
                Log.d("Scan", "Success upload images");

            }
        });
    }

    private void uploadSCan() {
        //FirebaseDate
        Timestamp fwaktu = Timestamp.now();

        SendDataScan sendDataScan = new SendDataScan(
                idCheckpoint.getText().toString(),
                titikKoordinat.getText().toString(),
                lokasi.getText().toString(),
                area.getText().toString(),
                team.getText().toString(),
                fwaktu,
                CurrentLat,
                CurrentLng,
                ttdText);

        DocumentReference kirim = dbs.collection("gtp")
                .document("data_checkpoint")
                .collection("checkpoint")
                .document(idDocument);
        kirim.update("checkPoint", FieldValue.arrayUnion(sendDataScan))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Data Berhasil Masuk", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Scan.this, Home_Gtp.class);
                            i.putExtra("idDoc", idDocument);
                            i.putExtra("displayName",team.getText().toString());
                            startActivity(i);
                            finish();
                        } else {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}