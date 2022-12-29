package com.example.isss.SI;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.isss.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class IsiTugas extends AppCompatActivity {
    String clickedId;

    //firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //collection templates
    CollectionReference tugas = db.collection("si").document("data_tugasTemplate").collection("tugasTemplate");

    CollectionReference update = db.collection("si").document("data_inspections").collection("inspections");

    TextView title,desc,tgl,pertanyaan,alamat;
    ImageView foto;
    Button selesai,ambilfoto,iconcamera;
    String sPhoto,ttd;

    //camera
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    //TTD
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_LOCATION = 2;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 3;
    StorageReference storageReference;
    SignatureView mSignaturePad;
    private static final int REQUEST_PERMISSIONS = 20;

    String jawaban;
    String idDocument;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_tugas);
        clickedId = getIntent().getStringExtra("clickedId");

        title = findViewById(R.id.titleT);
        desc = findViewById(R.id.descT);
        foto = findViewById(R.id.gambar1);
        selesai = findViewById(R.id.selesai);
        tgl = findViewById(R.id.tglInspec);
        pertanyaan = findViewById(R.id.qAction);
        alamat = findViewById(R.id.lokasiInspec);
        ambilfoto = findViewById(R.id.ambilfoto);
        iconcamera = findViewById(R.id.iconcamera);

        foto.setVisibility(View.GONE);
        iconcamera.setVisibility(View.GONE);

        tugas.document(clickedId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        Timestamp templateDate = (Timestamp) task.getResult().get("timeInspection");
                        String photo = (String) task.getResult().get("photo");

//                        if (task.getResult().get("statusTugas") == null){
//                            selesai.setVisibility(View.VISIBLE);
//                        }else{
//                            selesai.setVisibility(View.INVISIBLE);
//                        }
                           idDocument = task.getResult().get("inspectionsId").toString();
                           title.setText("Title :" + "\n"+task.getResult().get("titleTugas").toString());
                           desc.setText("Deskripsi :" + "\n"+task.getResult().get("deskripsi").toString());
                           tgl.setText("Tanggal Inspeksi :" + "\n"+templateDate.toDate());
                           pertanyaan.setText(task.getResult().get("questionAnswer").toString());
                           alamat.setText("Lokasi :" + "\n"+task.getResult().get("alamat").toString());

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference photoReference= storageReference.child("Photo/"+photo);
                        final long ONE_MEGABYTE = 1024 * 1024;
                        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                foto.setBackgroundResource(android.R.color.transparent);
                                foto.setImageBitmap(bmp);
                                foto.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
//                                Toast.makeText(getApplicationContext(), "Klik untuk mengambil foto ", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    }
                });

//        foto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////            ambilfoto();
//            }
//        });

        ambilfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilfoto();
            }
        });

        iconcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilfoto();
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foto.getVisibility() == View.GONE){
                    Toast.makeText(getApplicationContext(), "Klik Tombol ambil foto terlebih dahulu ", Toast.LENGTH_LONG).show();
                }else{
                    tambahcatatan();
                }
            }
        });
    }

    //FOTO
    private void ambilfoto() {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);

        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }


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
        IsiTugas.this.sendBroadcast(mediaScanIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            foto.setBackgroundResource(android.R.color.transparent);
            Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
            foto.setImageBitmap(photoBitmap);
            foto.setVisibility(View.VISIBLE);
            ambilfoto.setVisibility(View.GONE);
            iconcamera.setVisibility(View.VISIBLE);

            Log.d("testPhoto",photoBitmap.toString());
            UploadPhotoToCloudStore(photoBitmap);
        }
    }


    private void UploadPhotoToCloudStore(Bitmap photoBitmap) {
        final String namefile = String.format("Photo_%d.jpg", System.currentTimeMillis());
        sPhoto = namefile;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("Photo/" + namefile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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
                Log.d("Scan", "Success upload images");

            }
        });
    }

    //SIGNATUREPAD
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
    private void UploadSignatureToCloudStore(Bitmap signatureBitmap) {
        final String namefile = String.format("Signature_%d.jpg", System.currentTimeMillis());
        ttd = namefile;
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
                Log.d("Scan", "Success upload images");

            }
        });
    }

    void tambahcatatan() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(IsiTugas.this);
        alertDialog.setTitle("Berikan Catatan");

        final EditText catatan = new EditText(IsiTugas.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        catatan.setLayoutParams(lp);
        alertDialog.setView(catatan);

        alertDialog.setPositiveButton("Berikutnya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        jawaban = catatan.getText().toString();
                        ttd();
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

    void ttd() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(IsiTugas.this);
        alertDialog.setTitle("Tanda Tangan");

        mSignaturePad = new SignatureView( IsiTugas.this, null);
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
                            ActivityCompat.requestPermissions(IsiTugas.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_STORAGE_PERMISSION);
                        } else {
                            Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

                            if (addJpgSignatureToGallery(signatureBitmap)) {
                                Log.d("ttd", "masuknih");
                                UploadSignatureToCloudStore(signatureBitmap);
                                FieldValue itgl = FieldValue.serverTimestamp();

                                tugas.document(clickedId)
                                        .update("statusTugas","Selesai",
                                                "signatureTugas",ttd,
                                                "photo",sPhoto,
                                                "catatan",jawaban,
                                                "waktuSelesai",itgl);

                                update.document(idDocument).update("status","Fail close");

                                Intent selesai = new Intent(IsiTugas.this, InspeksiSelesai.class);
                                startActivity(selesai);
                                finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent kembali = new Intent(IsiTugas.this,Side_SI.class);
        startActivity(kembali);
    }
}